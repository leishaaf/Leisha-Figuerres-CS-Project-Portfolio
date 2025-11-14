package main

import (
	"fmt"

	"github.com/kljensen/snowball"
)

//word    [url]freq

func (invInd *Index) Search(targetword string) { // sets results map
	stemmedTar, err := snowball.Stem(targetword, "english", true) // stem the word first then direct search
	if err != nil {
		fmt.Println("Error stemming word")
	}
	if (invInd.index[stemmedTar]) == nil {
		fmt.Println("Word not found!")
		return
	}
	//invInd.searchResults = invInd.index[stemmedTar] // get the result invertedIndex after search
	for url, freq := range invInd.index[stemmedTar] {
		invInd.searchResults = append(invInd.searchResults, RowData{stemmedTar, url, "", invInd.docWordCount[url], freq}) // "" for titles
	}
	invInd.rankedResults = TfIdf(invInd.searchResults, invInd.docWordCount)
}

func (inv *Index) GetSearchResults() []RowData {
	return inv.searchResults
}
