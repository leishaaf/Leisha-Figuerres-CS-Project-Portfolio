package main

import (
	"fmt"
	"regexp"
	"strings"
	"time"
)

type Robot struct {
	patterns  []string // disallow and allow agent records
	delaySecs time.Duration
}

func NewRobot() *Robot {
	robot := &Robot{
		patterns:  []string{},
		delaySecs: 100 * time.Millisecond,
	}
	return robot
}

func (robot *Robot) ReadRecords(seed string) { // read the robots.txt for every site
	robotSeed := seed + "/robots.txt" // get the robots.txt url from seed
	robotBody := download(robotSeed)
	if robotBody == "" {
		return
	}
	lines := strings.Split(robotBody, "\n") // split by lines
	rulesApply := false                     // flag to check which rules apply to us
	for _, line := range lines {
		if strings.Contains(line, "User-agent: *") { // if the rules apply to us
			// fmt.Println("true")
			rulesApply = true
		}
		if !rulesApply {
			continue // if we're not the user agent specified, then ignore rules listed
		}
		fmt.Println(line)
		isColon := func(c rune) bool {
			return c == ':'
		}
		lineArr := strings.FieldsFunc(line, isColon)
		if strings.Contains(line, "Disallow") {
			pattern := strings.ReplaceAll(lineArr[1], "*", ".*")
			pattern = strings.TrimSpace(pattern)
			robot.patterns = append(robot.patterns, pattern) // add to slice
		}
		// if strings.Contains(line, "Crawl-delay") { // https://pkg.go.dev/strconv#ParseInt
		// 	secs, err := strconv.ParseFloat(strings.TrimSpace(lineArr[1]), 64)
		// 	if err != nil {
		// 		fmt.Println(err)
		// 		return
		// 	}
		// 	robot.delaySecs = time.Duration(secs * float64(time.Second)) // convert to seconds
		// }
		if rulesApply && line == "\n" || line == "" { // check if end of our rules
			break
		}
	}
}

func (robot *Robot) CrawlDelay() {
	time.Sleep(robot.delaySecs)
}

func (robot *Robot) Disallow(host string) bool {
	// check if host url == disallow or allow and if so skip
	for _, pattern := range robot.patterns {
		matched, err := regexp.MatchString(pattern, host)
		if err != nil {
			fmt.Println(err)
		}
		if matched {
			return matched
		}
	}
	return false
}
