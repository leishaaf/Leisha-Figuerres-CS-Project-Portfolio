package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"os"
)

type PromptBody struct {
	prompt string
	id     int
}

func ParseCSV(db *VectorDB) { // parse the csv file and inserts into vector db
	// values we're getting from each line is department, course name, instructor, times, and location
	file, err := os.Open("courses.csv")
	errCheck(err)
	defer file.Close()
	records := []PromptBody{}
	csvReader := csv.NewReader(file) // read csv line by line https://gosamples.dev/read-csv/
	id := 0
	for {
		record, err := csvReader.Read()
		if err == io.EOF {
			break
		}
		errCheck(err)
		if len(record) == 0 {
			return // something went wrong with parsing record
		}
		course := fmt.Sprintf("%s %s %s", record[0], record[1], record[6])
		instructor := fmt.Sprintf("%s %s", record[17], record[18])
		location := fmt.Sprintf("%s %s", record[14], record[15])
		times := fmt.Sprintf("%s %s-%s", record[8], record[10], record[9])
		promptBody := fmt.Sprintf("Course: %s Instructor: %s Meeting Days: %s Location: %s", course, instructor, times, location)
		toInsert := PromptBody{promptBody, id}
		records = append(records, toInsert)
		id++ // increment id
	}
	batchNum := 200
	for i := 0; i < len(records); i += batchNum { // batch embedd 100 rows at a time
		max := i + batchNum     // we're doing 100 at a time so we need to get the part of the mass record slice from i- i+100
		if max > len(records) { // make sure we don't go over the length of records
			max = len(records)
		}
		recordsToSend := records[i:max]               // here is where we get the subarry from i - i +100
		prompts := make([]string, len(recordsToSend)) // create a slice of strings w/len of recordsToSend
		for ind, r := range recordsToSend {
			prompts[ind] = r.prompt
		}
		blobs := db.CreateBlobs(prompts)
		for ind, blob := range blobs { // create blob for each prompt and insert it
			db.Insert(recordsToSend[ind].id, blob, recordsToSend[ind].prompt)
		}
	}

}
