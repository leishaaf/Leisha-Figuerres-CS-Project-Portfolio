package main

import (
	"bufio"
	"os"

	"github.com/sashabaranov/go-openai"
)

func main() {
	name := "vector.db"
	db := NewVectorDB(name)
	db.CreateTables()
	//ParseCSV(db) // populates the db
	scanner := bufio.NewScanner(os.Stdin)
	apiKey := os.Getenv("OPENAI_PROJECT_KEY")
	client := openai.NewClient(apiKey)
	for scanner.Scan() { // get each line of course info from csv
		question := string(scanner.Text())
		DefineSchema(question, apiKey, client, db) // define the schema for this question and calls chat()
	}
}
