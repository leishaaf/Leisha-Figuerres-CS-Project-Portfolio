package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"os"

	sqlite_vec "github.com/asg017/sqlite-vec-go-bindings/cgo"
	_ "github.com/mattn/go-sqlite3"
	"github.com/sashabaranov/go-openai"
)

type VectorDB struct {
	client *openai.Client
	db     *sql.DB
}

func errCheck(err error) { // to make err checking more concise
	if err != nil {
		log.Fatalf("%s", err) // exit program upon error
	}
}

func NewVectorDB(name string) *VectorDB { // creates vector db
	sqlite_vec.Auto()
	//os.Remove(name)
	db, err := sql.Open("sqlite3", name)
	errCheck(err)

	return &VectorDB{
		client: openai.NewClient(os.Getenv("OPENAI_PROJECT_KEY")), // holds my api key
		db:     db,
	}
}

func (vectorDB *VectorDB) CreateTables() { // our sql db stores vectors that measure similarity
	// insert each line in csv into courses
	// embedding for each course
	_, err := vectorDB.db.Exec(`
		CREATE VIRTUAL TABLE IF NOT EXISTS courses USING vec0(
			id INTEGER PRIMARY KEY,
			embedding FLOAT[3072],
			source_text TEXT
		);
	`)
	errCheck(err)
}
func (vectorDB *VectorDB) CreateBlob(str string) []byte {
	req := openai.EmbeddingRequest{
		Input: str,
		Model: openai.LargeEmbedding3,
	}

	resp, err := vectorDB.client.CreateEmbeddings(context.TODO(), req)
	errCheck(err)

	blob, err := sqlite_vec.SerializeFloat32(resp.Data[0].Embedding)
	errCheck(err)
	return blob
}
func (vectorDB *VectorDB) Insert(id int, blob []byte, sourceText string) { // added transactions upon insert to make it faster
	_, err := vectorDB.db.Exec(`
		INSERT INTO courses (id, embedding, source_text) VALUES (?, ?, ?) `, id, blob, sourceText)

	errCheck(err)
	fmt.Printf("inserting %s", sourceText)
}
func (vectorDB *VectorDB) CreateBlobs(plain []string) [][]byte { // from in class
	req := openai.EmbeddingRequest{
		Model: openai.LargeEmbedding3,
		Input: plain,
	}

	resp, err := vectorDB.client.CreateEmbeddings(context.TODO(), req)
	if err != nil {
		log.Fatal(err)
	}

	blobs := make([][]byte, len(plain))
	for i := range plain {
		blobs[i], err = sqlite_vec.SerializeFloat32(resp.Data[i].Embedding)
		if err != nil {
			log.Fatal(err)
		}
	}
	return blobs
}

func (vectorDB *VectorDB) Query(question string) []string {
	similarRows := []string{}
	blob := vectorDB.CreateBlob(question) // implement embedding for the question user asks

	rows, err := vectorDB.db.Query(`
		SELECT id, source_text FROM courses WHERE embedding
		MATCH ? ORDER BY distance LIMIT 35;
	`, blob)
	errCheck(err)
	for rows.Next() {
		var id int
		var sourceText string
		rows.Scan(&id, &sourceText)
		similarRows = append(similarRows, sourceText) // add prompt to the similar rows
	}
	return similarRows
}
