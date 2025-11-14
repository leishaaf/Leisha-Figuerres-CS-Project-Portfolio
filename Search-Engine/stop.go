package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"os"

	"github.com/kljensen/snowball"
)

func Stop(stem string, stopWords map[string]struct{}) bool {
	_, ok := stopWords[stem] // check if word is a stop word and if so don't add to index
	return (ok)
}

func BuildStop() map[string]struct{} {
	stopWords := make(map[string]struct{})
	// https://pkg.go.dev/encoding/json
	// https://gobyexample.com/json for unmarshalling - parses JSON-encoded data and stores the result in the value of specified data structure that the second argument points to
	file, err := os.Open("stopwords-en.json")
	if err != nil {
		log.Fatal("Error opening file", err)
	}
	defer file.Close()
	stopList := []string{}
	stopWordSlice, err := io.ReadAll(file)
	if err != nil {
		log.Fatal("Error reading file", err) // log so terminates program properly
	}
	err = json.Unmarshal(stopWordSlice, &stopList) // parses json read from file earlier into the stopList slice
	if err != nil {
		log.Fatal("Error unmarshalling data", err)
	}
	for _, stopWord := range stopList { // copy into the stopWord map for efficient checking
		stemmedStop, err := snowball.Stem(stopWord, "english", true) // stem the word first then direct search
		if err != nil {                                              // stem so searching if word is stopword is easier when building index
			fmt.Println("Error stemming word")
		}
		stopWords[stemmedStop] = struct{}{} // copies into map of index
	}
	return stopWords
}
