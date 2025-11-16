package main

import (
	"github.com/kljensen/snowball"
)

type Index struct { // where the indexing happens
	index         map[string](map[string]int)
	docWordCount  map[string]int
	searchResults []RowData
	stopWords     map[string]struct{} // good on memory
	rankedResults []Match             // slice of the ranked terms based on relevancy calculated in TfIdf()
}

func NewIndex() *Index { // fill the inverted index
	invInd := &Index{
		index:         make(map[string]map[string]int),
		docWordCount:  make(map[string]int),
		searchResults: []RowData{},
		stopWords:     BuildStop(), // read and build the stop list
		rankedResults: []Match{},
	}
	return invInd
}

func (invInd *Index) Insert(url, title string, words []string) { // builds the inverted index & make the map of the doc -> word count
	wordCount := 0
	for _, word := range words {
		stemmed, err := snowball.Stem(word, "english", true) // stem word
		if err != nil {
			continue
		}
		if Stop(stemmed, invInd.stopWords) { // continue if stem is a stop word
			continue
		}
		if invInd.index[stemmed] == nil { // make sure to not overwrite
			invInd.index[stemmed] = make(map[string]int)
		}
		invInd.index[stemmed][url]++ // increment the frequency
		wordCount++                  // count words in doc
	}
	invInd.docWordCount[url] = wordCount
}
func (invInd *Index) GetRankedResults() []Match {
	return invInd.rankedResults
}
