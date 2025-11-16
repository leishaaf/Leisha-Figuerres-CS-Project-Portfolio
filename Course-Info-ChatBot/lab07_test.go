package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"testing"

	"github.com/sashabaranov/go-openai"
)

var db *VectorDB

func getKeyAndClient() (string, *openai.Client) {
	apiKey := os.Getenv("OPENAI_PROJECT_KEY")
	client := openai.NewClient(apiKey)
	return apiKey, client
}

func testChat(apiKey string, client *openai.Client, question, sourceText string) string { // for the judge prompt
	sourceText = fmt.Sprintf("%s\n %s", sourceText, question) // combine the prompt and data from file
	req := openai.ChatCompletionRequest{                      // create the request
		Model: openai.GPT4o,
		Messages: []openai.ChatCompletionMessage{
			{
				Role:    openai.ChatMessageRoleUser,
				Content: sourceText, // feed the file to LLM
			},
		},
	}
	resp, err := client.CreateChatCompletion(context.TODO(), req)
	if err != nil {
		log.Fatalf("%s", err) // exit early if problem with response
	}
	response := resp.Choices[0].Message.Content
	fmt.Println(response)
	return response
}

func TestMain(m *testing.M) {
	name := "test.db"
	os.Remove(name)
	db = NewVectorDB("test.db")
	db.CreateTables()

	ParseCSV(db) // populates the db
	defer db.db.Close()
	m.Run()
}
func TestLab07(t *testing.T) {
	tests := []struct {
		name         string
		prompt       string
		wantResponse string
		want         []string
	}{
		{
			name:   "TestPhil",
			prompt: "What courses is Phil Peterson teaching in Fall 2024?",
			wantResponse: `- Course: CS 272 Software Development; Instructor: Philip Peterson; Meeting Times/Days: IP 0800-TR; Location: HR 148
- Course: CS 272 Software Development; Instructor: Philip Peterson; Meeting Times/Days: IP 1440-TR; Location: HR 148
- Course: CS 272L Software Development Lab; Instructor: Philip Peterson; Meeting Times/Days: IP 1300-W; Location: ED 104
- Course: CS 272L Software Development Lab; Instructor: Philip Peterson; Meeting Times/Days: IP 1455-W; Location: ED 104`,
			want: []string{"High", "Medium"},
		},
		{
			name:   "TestPHIL",
			prompt: "Which philosophy courses are offered this semester?",
			wantResponse: `- PHIL 110 Great Philosophical Questions — Instructor: Thomas Cavanaugh — Meeting: IP 0800-TR — Location: LME 1225
- PHIL 110 Great Philosophical Questions — Instructor: Richie Kim — Meeting: IP 1415-MWF — Location: KA 267
- PHIL 110 Great Philosophical Questions — Instructor: Deena Lin — Meeting: IP 1245-TR — Location: KA 311
- PHIL 110 Great Philosophical Questions — Instructor: Purushottama Bilimoria — Meeting: IP 1635-TR — Location: CO 418
- PHIL 110 Great Philosophical Questions — Instructor: Purushottama Bilimoria — Meeting: IP 1830-TR — Location: CO 418
- PHIL 202 Philosophy of Religion — Instructor: Deena Lin — Meeting: IP 0955-TR — Location: KA 267
- PHIL 203 Social & Political Philosophy — Instructor: Ronald Sundstrom — Meeting: IP 1245-TR — Location: ED 102
- PHIL 204 Philosophy of Science — Instructor: Krupa Patel — Meeting: IP 1530-MWF — Location: KA 111
- PHIL 205 Philosophy of Biology — Instructor: Stephen Friesen — Meeting: IP 1030-MWF — Location: KA 167
- PHIL 206 The Human Animal — Instructor: Jennifer Fisher — Meeting: IP 1030-MWF — Location: KA 363
- PHIL 209 Aesthetics — Instructor: Brian Pines — Meeting: IP 1440-TR — Location: KA 363
- PHIL 209 Aesthetics — Instructor: Brian Pines — Meeting: IP 1635-TR — Location: KA 363
- PHIL 220 Asian Philosophy — Instructor: Jea Oh — Meeting: IP 1030-MWF — Location: KA 111
- PHIL 230 Philosophy of Human Person — Instructor: Laurel Scotland-Stewart — Meeting: IP 1415-MWF — Location: CO 314
- PHIL 230 Philosophy of Human Person — Instructor: Laurel Scotland-Stewart — Meeting: IP 1530-MWF — Location: CO 314
- PHIL 240 Ethics — Instructor: Jea Oh — Meeting: IP 1300-MWF — Location: CO 314
- PHIL 240 Ethics — Instructor: Greig Mulberry — Meeting: IP 1830-MW — Location: CO 314
- PHIL 240 Ethics — Instructor: Greig Mulberry — Meeting: IP 1645-MW — Location: CO 314
- PHIL 240 Ethics — Instructor: Joshua Carboni — Meeting: IP 1440-TR — Location: CO 414
- PHIL 240 Ethics — Instructor: Joshua Carboni — Meeting: IP 1600-M — Location: ST 112
- PHIL 240 Ethics — Instructor: Krupa Patel — Meeting: OL — Location: ONL ONL
- PHIL 240 Ethics — Instructor: Nick Leonard — Meeting: OL — Location: ONL ONL
- PHIL 240 Ethics — Instructor: Richie Kim — Meeting: IP 1530-MWF — Location: KA 267
- PHIL 240 Ethics — Instructor: Jennifer Fisher — Meeting: IP 1145-M — Location: KA 363
- PHIL 244 Environmental Ethics — Instructor: Stephen Friesen — Meeting: IP 0915-MWF — Location: KA 167
- PHIL 310 Ancient & Medieval Philosophy — Instructor: Thomas Cavanaugh — Meeting: IP 0955-TR — Location: LM 147
- PHIL 317 Philosophy of Emotion — Instructor: David Kim — Meeting: IP 1245-TR — Location: ED 104
- PHIL 319 Logic — Instructor: Nick Leonard — Meeting: IP 1415-MWF — Location: LME 1225
- PHIL 399 Dignity in Social Context — Instructor: Ronald Sundstrom — Meeting: IP (TBA) — Location: TBA
- PHIL 482 Topics in the History of Phil — Instructor: Geoffrey Ashton — Meeting: IP 1145-MWF — Location: CO 413
- PHIL 484 Topics in Ethics — Instructor: Ronald Sundstrom — Meeting: IP 1440-TR — Location: ED 102`,
			want: []string{"High", "Medium"},
		},
		{
			name:   "TestBio",
			prompt: "Where does Bioinformatics meet?",
			wantResponse: `- Course: BTEC 640 Bioinformatics
- Instructor: Patricia Francis-Lyon
- Meeting Times/Days: IP 1645-MW
- Location: LM 365`,
			want: []string{"High", "Medium"},
		},
		{
			name:   "TestMultiple",
			prompt: "I would like to take a Rhetoric course from Phil Choong. What can I take?",
			wantResponse: `- Course: RHET 103 Public Speaking
  Instructor: Philip Choong
  Meeting Days/Times: IP 0955-TR
  Location: ED 006

- Course: RHET 103 Public Speaking
  Instructor: Philip Choong
  Meeting Days/Times: IP 1440-TR
  Location: LM 350`,
			want: []string{"High", "Medium"},
		},
	}
	apiKey, client := getKeyAndClient()
	for _, test := range tests {
		response := DefineSchema(test.prompt, apiKey, client, db) // calls chat()
		judgePrompt := fmt.Sprintf("You are an extremely non picky similarity judge. Compare these two response and answer based on rules. If two responses contains the same items (if they have any item/items in common) -- even if phrased differently or even if phrased identically, answer 'High' For things to be high they do not need to be exactly alike. However, if they are somehow identical, also answer 'High'. If two responses have similar meaning but phrased differently, answer 'Medium'. If one response contains none of the items in the 2nd response, answer 'Low'. %s vs. %s", response, test.wantResponse)
		//judgePrompt := fmt.Sprintf("You are a similarity judge. Compare these two response and answer based on rules. If one response contains items in another response answer 'High'. If one answer has similar meanings but are phrased differently, answer 'Medium'. If there are no common items between both responses or they don't have slightly similar meetings, answer 'Low'. %s vs. %s", response, test.wantResponse)
		got := testChat(apiKey, client, test.prompt, judgePrompt)
		if got != test.want[0] && got != test.want[1] { // if the similarity score != medium ||!= high
			t.Errorf("Failed Similary Test. Prompt = %s and got %s and wanted %s. Similarity score = %s but should be %s", test.prompt, response, test.wantResponse, got, test.want)
		}
	}
}
