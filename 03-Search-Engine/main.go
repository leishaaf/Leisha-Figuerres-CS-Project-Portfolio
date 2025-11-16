/*
author@: leishaaf
project03: implementing search engine with sqlite db in addition to map of maps
*/

package main

import (
	"database/sql"
	"flag"
	"fmt"
	"html/template"
	"log"
	"net/http"
	"os"
	"time"

	_ "github.com/mattn/go-sqlite3"
)

func startServer(ind SearchEngine) { // takes any type that implements search engine
	http.Handle("/", http.FileServer((http.Dir("./static")))) // endpoints
	http.Handle("/top10/", http.StripPrefix("/top10/", http.FileServer(http.Dir("./static/top10"))))
	t := template.Must(template.ParseFiles("./static/template.html"))
	http.HandleFunc("/search", func(w http.ResponseWriter, r *http.Request) {
		query := r.URL.Query().Get("query")
		ind.Search(query)
		if len(ind.GetRankedResults()) == 0 { // target word didn't exist
			fmt.Fprintln(w, "Word", query, "not found in doc")
		} else {
			results := ind.GetRankedResults()
			t.Execute(w, results)

		}
	})
	go http.ListenAndServe(":8080", nil) // so that we can crawl and search at same time without interruptions
}

func errCheck(err error) { // to make err checking more concise
	if err != nil {
		log.Fatalf("%s", err)
	}
}

func main() {
	index := flag.String("index", "inmem", "sqlite")
	flag.Parse()
	fmt.Println("MODE: ", *index)
	host := "http://www.usfca.edu"
	//"http://localhost:8080/top10/"
	//"http://www.usfca.edu"
	crawler := NewCrawler()
	switch *index {
	case "inmem":
		ind := NewIndex()
		startServer(ind)
		crawler.crawl(host, ind)
	case "sqlite":
		dbName := "index.db" // dont reset database
		os.Remove(dbName)    // reset db everytime
		db, err := sql.Open("sqlite3", "index.db")
		errCheck(err)
		defer db.Close()
		_, err = db.Exec(`PRAGMA foreign_keys = ON;`)
		errCheck(err)
		_, err = db.Exec(`
			CREATE TABLE IF NOT EXISTS words(
				word_id INTEGER PRIMARY KEY,
				word TEXT UNIQUE
			);
		`)
		errCheck(err)
		_, err = db.Exec(`
			CREATE TABLE IF NOT EXISTS urls(
			url_id INTEGER PRIMARY KEY,
			url TEXT UNIQUE,
			title TEXT,
			word_count INTEGER
			);
		`)
		errCheck(err)
		_, err = db.Exec(`
			CREATE TABLE IF NOT EXISTS Frequencies(
				freq_id INTEGER PRIMARY KEY,
				freq INTEGER, 
				word_id INTEGER NOT NULL,
				url_id INTEGER NOT NULL,
				UNIQUE(word_id, url_id),
				FOREIGN KEY (word_id) REFERENCES words (word_id),
				FOREIGN KEY (url_id) REFERENCES urls (url_id)
			);
		`)
		errCheck(err)
		sqlInd := NewSqlIndex(db)
		startServer(sqlInd)
		go crawler.crawl(host, sqlInd)
	}
	for { // so that the server doesn't get killed when main thread exits (crawling stops)
		time.Sleep(100 * time.Millisecond)
	}
}
