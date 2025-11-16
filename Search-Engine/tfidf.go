package main

import (
	"math"
	"sort"
)

//func TfIdf(searchResults, docWordCount map[string]int, targetWord string, titleMap map[string]string)

func TfIdf(searchResults []RowData, docWordCount map[string]int) []Match { // rank the search results using computation
	rankedResults := []Match{}
	for _, result := range searchResults {
		tf := float64(result.freq) / float64(result.wordCount)
		idf := math.Log(float64(len(docWordCount))/float64(len(searchResults)) + 1) // add 1 inside log to get pos numbers
		score := tf * idf
		match := NewMatch(result.targetWord, result.url, result.title, score)
		rankedResults = append(rankedResults, *match) // add term to slice

	}
	sort.Sort(ByScore(rankedResults))
	return rankedResults
}
