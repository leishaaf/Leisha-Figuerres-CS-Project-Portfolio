package main

import (
	"github.com/kljensen/snowball"
	_ "github.com/mattn/go-sqlite3"
)

type RowData struct {
	targetWord, url, title string
	wordCount, freq        int
}

func (sqlInd *SqlIndex) Search(targetWord string) {
	sqlInd.searchResults = []RowData{}                            // slice to hold data of rows queried in search so we can compute tfidf
	stemmedTar, err := snowball.Stem(targetWord, "english", true) // stem the word first then direct search
	errCheck(err)
	rows, err := sqlInd.db.Query(`
		SELECT
			frequencies.freq,
			urls.word_count,
			urls.title,
			urls.url
		FROM frequencies 
		JOIN urls ON frequencies.url_id = urls.url_id 
		JOIN words ON frequencies.word_id = words.word_id
		WHERE words.word = ?
	`, stemmedTar) // joins the tables on the foreign keys url_id and word_id and search for target : finds all the matching ids and connects them so now we can get word
	errCheck(err)
	defer rows.Close()
	// joins https://www.sqlitetutorial.net/sqlite-join/
	for rows.Next() { // return all words that contain word
		var url string
		var freq int
		var wordCount int
		var title string
		err = rows.Scan(&freq, &wordCount, &title, &url) // scans what we needed from select statement
		sqlInd.searchResults = append(sqlInd.searchResults, RowData{stemmedTar, url, title, wordCount, freq})
		errCheck((err))
	}
	sqlInd.rankedResults = TfIdf(sqlInd.searchResults, sqlInd.docWordCount)
}
