package main

import (
	"database/sql"
	"fmt"

	"github.com/kljensen/snowball"
	_ "github.com/mattn/go-sqlite3"
)

type SqlIndex struct {
	db            *sql.DB
	searchResults []RowData
	docWordCount  map[string]int
	stopWords     map[string]struct{} // good on memory
	rankedResults []Match
}

func NewSqlIndex(db *sql.DB) *SqlIndex {
	sqlInd := &SqlIndex{
		docWordCount: make(map[string]int),
		db:           db,
		stopWords:    BuildStop(), // read and build the stop list
	}
	return sqlInd
}

func (sqlInd *SqlIndex) Insert(url, title string, words []string) { // build all three tables
	var urlId int
	// wrap all inserting and querying into one transaction
	tx, err := sqlInd.db.Begin() // https://go.dev/doc/database/execute-transactions
	fmt.Println("IN SQL INSERTING", url)
	defer tx.Rollback()
	errCheck(err)
	_, err = tx.Exec(`INSERT INTO urls(url, title, word_count) VALUES(?, ?, 0) ON CONFLICT(url) DO NOTHING;`, url, title)
	errCheck(err)
	row := tx.QueryRow("SELECT url_id FROM urls WHERE url = ?", url)
	err = row.Scan(&urlId)
	errCheck(err)
	wordCount := 0
	for _, word := range words {
		stemmed, err := snowball.Stem(word, "english", true) // stem word
		errCheck(err)
		if Stop(stemmed, sqlInd.stopWords) { // continue if stem is a stop word
			continue
		}
		// handling upserts https://www.sqlitetutorial.net/sqlite-upsert/
		_, err = tx.Exec(`INSERT INTO words(word) VALUES(?) ON CONFLICT(word) DO NOTHING;`, stemmed) // dont add
		errCheck(err)
		var wordId int // get the word_id from word table by scanning row if unique
		row := tx.QueryRow("SELECT word_id FROM words WHERE word = ?", stemmed)
		err = row.Scan(&wordId)
		errCheck(err)
		_, err = tx.Exec(`INSERT INTO frequencies(freq, word_id, url_Id) VALUES(1, ?, ?) ON CONFLICT(word_id, url_id) DO UPDATE SET freq = freq + 1;`, wordId, urlId) // if unique add to table with value of one, if not increment freq by one
		errCheck(err)
		wordCount++ // count words in doc
	}
	sqlInd.docWordCount[url] = wordCount
	_, err = tx.Exec(`UPDATE urls SET word_count = ? WHERE url_id = ?;`, wordCount, urlId)
	errCheck(err)
	err = tx.Commit() // end transaction
	errCheck(err)
}

func (sqlInd *SqlIndex) GetRankedResults() []Match {
	return sqlInd.rankedResults
}
