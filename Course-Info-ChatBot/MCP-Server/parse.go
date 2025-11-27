/*
* @authors: leishaaf
* file: parse.go
* code that reads the courses.csv file, parses each row, and creates a string with the important information from it.
* string created, is inserted into the course table along with embedding (after batch embedding)
 */

package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"os"
	"path/filepath"
)

type PromptBody struct {
	prompt string
	id     int
}

/*
* method: ParseCSV()
* method that reads the course.csv file and parses each row, then inserts it into the db
* utilizes batch embedding
 */
func ParseCSV(db *VectorDB) { // parse the csv file and inserts into vector db
	// values we're getting from each line is department, course name, instructor, times, and location
	exe, err := os.Executable() // locate the file of courses.csv on users computer by getting the full path to the current running binary
	errCheck(err)
	dir := filepath.Dir(exe)
	csvPath := filepath.Join(dir, "courses.csv") // Users/leisha/CS272/lab08-leishaaf
	file, err := os.Open(csvPath)
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
		promptBody := fmt.Sprintf("SUBJ: %s, CRSE NUM: %s, SEC: %s, Title Short Desc: %s, Meet Days: %s, Begin Time: %s, End time: %s, BLDG: %s, RM: %s, Primary Instructor First Name: %s, Primary Instructor Last Name: %s, Primary Instructor Email: %s", record[0], record[1], record[2], record[6], record[8], record[9], record[10], record[14], record[15], record[17], record[18], record[19])
		toInsert := PromptBody{promptBody, id}
		records = append(records, toInsert)
		id++ // increment id
	}
	batchNum := 100
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
	fmt.Fprintln(os.Stderr, "Done Inserting.")
}
