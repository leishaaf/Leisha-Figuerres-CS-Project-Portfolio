package main

type Match struct {
	MatchName string
	DocName   string
	DocTitle  string
	Score     float64
}

func NewMatch(matchName, docName, docTitle string, score float64) *Match { // so that we can sort
	term := &Match{
		MatchName: matchName,
		DocName:   docName,
		DocTitle:  docTitle,
		Score:     score,
	}
	return term
}

type ByScore []Match // implements sorting interface, used example from go.dev

func (a ByScore) Len() int {
	return len(a)
}

func (a ByScore) Swap(i, j int) {
	a[i], a[j] = a[j], a[i]
}

func (a ByScore) Less(i, j int) bool {
	if a[i].Score == a[j].Score {
		return a[i].DocName > a[j].DocName
	}
	return a[i].Score > a[j].Score // scores go from highest to lowest
}
