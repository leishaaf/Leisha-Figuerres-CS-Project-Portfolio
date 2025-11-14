package main

type SearchEngine interface {
	Insert(url, title string, words []string) // build data structure/db you're using for querying
	Search(string)
	GetRankedResults() []Match
}
