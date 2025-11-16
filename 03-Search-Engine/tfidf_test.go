package main

import (
	"reflect"
	"testing"
)

func TestTfIdfs(t *testing.T) {
	tests := []struct {
		name, targetWord, seedUrl string
		want                      []Match
	}{
		{
			"TEST TFIDF 1",
			"Verona",
			"https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.0.html",
			[]Match{
				{
					MatchName: "verona",
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.0.html",
					Score:     0.01050223000848402,
				},
			},
		},
		{
			"TEST TFIDF 2", // word not in doc
			"Hawai'i",
			"https://usf-cs272-f25.github.io/test-data/rnj/",
			[]Match{},
		},
		{
			"TEST TFIDF 3", // word not in doc
			"Romeo",
			"https://usf-cs272-f25.github.io/test-data/rnj/",
			[]Match{
				{
					MatchName: "romeo", // 1
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneII_30.2.html",
					Score:     0.04555896961426871,
				},
				{
					MatchName: "romeo", // 2
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneII_30.1.html",
					Score:     0.04305261991055561,
				},
				{
					MatchName: "romeo", // 3
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneII_30.0.html",
					Score:     0.03353937970451348,
				},
				{
					MatchName: "romeo", //4
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.2.html",
					Score:     0.032289464932916706,
				},
				{
					MatchName: "romeo", //5
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.4.html",
					Score:     0.029023404112115934,
				},
				{
					MatchName: "romeo", //6
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneII_30.3.html",
					Score:     0.024353819857511592,
				},
				{
					MatchName: "romeo", //7
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.5.html",
					Score:     0.021797081149683813,
				},
				{
					MatchName: "romeo", //8
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.0.html",
					Score:     0.02100446001696804,
				},
				{
					MatchName: "romeo", //9
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.1.html",
					Score:     0.019181431411721756,
				},
				{
					MatchName: "romeo", //10
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/",
					Score:     0.01730476589279,
				},
				{
					MatchName: "romeo", //11
					DocName:   "https://usf-cs272-f25.github.io/test-data/rnj/sceneI_30.3.html",
					Score:     0.003716606866273165,
				},
			},
		},
	}
	for _, test := range tests {
		crawler := NewCrawler()
		ind := NewIndex()
		crawler.crawl(test.seedUrl, ind)
		ind.Search(test.targetWord)
		got := ind.rankedResults
		if !reflect.DeepEqual(got, test.want) {
			t.Errorf("tfidf() with term %s and got %v, but wanted %v", test.targetWord, got, test.want)
		}
	}

}
