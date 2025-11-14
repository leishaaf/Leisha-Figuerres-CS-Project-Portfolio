package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"time"

	"github.com/sashabaranov/go-openai"
	"github.com/sashabaranov/go-openai/jsonschema"
)

var start time.Time

func GetRows(tc openai.ToolCall, db *VectorDB) string {
	args := map[string]interface{}{}
	err := json.Unmarshal([]byte(tc.Function.Arguments), &args)
	errCheck(err)
	question := args["question"].(string) // get the question from user
	similarRows := db.Query(question)
	rowJson, err := json.Marshal(similarRows) // turn the similar rows slice into json object https://golang.cafe/blog/golang-json-marshal-example
	errCheck(err)
	return string(rowJson) // stringify json
}

func DefineSchema(prompt, apiKey string, client *openai.Client, db *VectorDB) string { // defines the schema
	// @author: phil peterson for structure
	params := jsonschema.Definition{
		Type: jsonschema.Object,
		Properties: map[string]jsonschema.Definition{
			"question": {
				Type:        jsonschema.String,
				Description: "Question user asked us about course info",
			},
		},
		Required: []string{"question"},
	}

	// Make a function using those parameters
	getRows := openai.FunctionDefinition{
		Name:        "get_rows",
		Description: "Answer to user's question using Course, Instructor, Meeting Times/Days, and Location",
		Parameters:  params,
	}

	// Make a tool using the function
	tool := openai.Tool{
		Type:     openai.ToolTypeFunction,
		Function: &getRows,
	}
	// Use a slice of ChatCompletionMessage to maintain context with the LLM
	dialogue := []openai.ChatCompletionMessage{
		{
			Role:    openai.ChatMessageRoleUser,
			Content: prompt,
		},
		{
			Role:    openai.ChatMessageRoleUser,
			Content: "Do not use any markdown or formatting symbols.", // after changing model to chatgpt4o, it would use formatting symbols like ***, so needed to add system promptwh
		},
	}
	start = time.Now() // start the timer
	// extract the tool request
	req := openai.ChatCompletionRequest{ // create the request
		Model:    openai.GPT4o,
		Messages: dialogue,
		Tools:    []openai.Tool{tool},
	}
	resp, err := client.CreateChatCompletion(context.TODO(), req) // first request to get the similar rows
	errCheck(err)
	msg := resp.Choices[0].Message

	if len(msg.ToolCalls) != 1 { // error checking
		log.Fatal("expected one tool call")
	}

	dialogue = append(dialogue, msg)

	newmsg := openai.ChatCompletionMessage{
		Role:       openai.ChatMessageRoleTool,
		Content:    GetRows(msg.ToolCalls[0], db),
		Name:       msg.ToolCalls[0].Function.Name,
		ToolCallID: msg.ToolCalls[0].ID,
	}

	dialogue = append(dialogue, newmsg)
	response := chat(apiKey, client, dialogue, tool) // calls chat() which generates the response
	return response
}

func chat(apiKey string, client *openai.Client, dialogue []openai.ChatCompletionMessage, tool openai.Tool) string {
	newreq := openai.ChatCompletionRequest{
		Model:    openai.GPT4o,
		Messages: dialogue,
		Tools:    []openai.Tool{tool},
	}
	resp, err := client.CreateChatCompletion(context.TODO(), newreq)
	if err != nil {
		log.Fatal(err)
	}
	end := time.Since(start) // end time of request
	// get the amount of tokens used for response  generated
	response := resp.Choices[0].Message.Content
	fmt.Println(response)
	fmt.Printf("Time: %v\n", end)
	fmt.Printf("Tokens: %d\n", resp.Usage.TotalTokens)
	return response
}
