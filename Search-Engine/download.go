package main

import (
	"fmt"
	"strings"
	"unicode"

	"golang.org/x/net/html"
	"golang.org/x/net/html/atom"
)

func extract(body string) ([]string, []string, string) { // this first, calls clean() inside
	// get all words and urls. slice of words and slice of urls
	words := []string{}
	hrefs := []string{}
	title := ""
	doc, err := html.Parse(strings.NewReader(body)) // from goLang html package documentation
	if err != nil {
		fmt.Println("ERROR READING DOC ", err)
	}
	for node := range doc.Descendants() { // doc.Descendants retrieves all descendant elements regardless of depth. so traverses down whole html tree
		if node.Type == html.ElementNode && node.DataAtom == atom.A { // add the hrefs to hrefs slice
			for _, a := range node.Attr {
				if a.Key == "href" {
					hrefs = append(hrefs, a.Val) // changing from regular split
				}
			}
		}
		if node.Type == html.TextNode {
			if node.Parent.Data == "style" || node.Parent.Data == "script" {
				continue // if the parent data type of text node is style or script, skip adding to word slice
			}
			trimmedWord := strings.TrimSpace(node.Data)
			if trimmedWord == "" {
				continue
			}
			wordSlice := strings.Fields(trimmedWord) // switched to Fields & Field.func to make code safer as project grows larger per suggestion
			for _, word := range wordSlice {
				normalizedWord := ""
				for _, r := range word {
					if unicode.IsLetter(r) || unicode.IsNumber(r) || unicode.IsDigit(r) { // get rid of all punctuation
						normalizedWord += string(r)
					}
				}
				if normalizedWord != "" { // check for empty strings and white spaces
					words = append(words, normalizedWord)
				}
			}
			if node.Parent.Data == "title" { // get the title
				title = node.Data
			}
			// if not title
		}
	}
	return words, hrefs, title
}
