package main

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"

	mcp_golang "github.com/metoro-io/mcp-golang"
	"github.com/metoro-io/mcp-golang/transport/stdio"
)

type CourseInfoArgs struct {
	Submitter string `json:"submitter" jsonschema:"required,description=The name of the thing calling this tool (openai, google, claude, etc)"`
	Content   string `json:"content" jsonschema:"required,description=The content of the message"`
}

func GetRows(content string, db *VectorDB) string { // function for the tool calling
	similarRows := db.Query(content)
	rowJson, err := json.Marshal(similarRows) // turn the similar rows slice into json object https://golang.cafe/blog/golang-json-marshal-example
	errCheck(err)
	return string(rowJson) // stringify json
}

func main() {
	done := make(chan struct{})
	exe, err := os.Executable()
	errCheck(err)
	dir := filepath.Dir(exe)
	dbPath := filepath.Join(dir, "vector.db") // locate where vector.db should be added on users computer
	apiKey := os.Getenv("OPENAI_PROJECT_KEY")
	db := NewVectorDB(dbPath, apiKey) // Use absolute path : Users/leisha/CS272/lab08-leishaaf
	db.CreateTable()
	ParseCSV(db)
	server := mcp_golang.NewServer(stdio.NewStdioServerTransport())
	// structure from example given to us in class and by https://github.com/metoro-io/mcp-golang/tree/main
	err = server.RegisterTool("get_rows", "Answer the users question using the similar rows given to you", func(arguments CourseInfoArgs) (*mcp_golang.ToolResponse, error) {
		rowsJson, err := json.Marshal(db.Query(arguments.Content))
		if err != nil {
			fmt.Fprintln(os.Stderr, "JSON marshal failed:", err)
			return nil, err
		}
		text := mcp_golang.NewTextContent(string(rowsJson))
		resp := mcp_golang.NewToolResponse(text)
		fmt.Fprintln(os.Stderr, "Tool called with:", arguments.Content)
		return resp, nil
	})
	errCheck(err)
	err = server.Serve()
	errCheck(err)
	<-done
}
