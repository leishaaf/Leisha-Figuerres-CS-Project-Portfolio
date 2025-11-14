package main

import (
	"fmt"
	"sync"
	"time"
)

type Crawler struct {
	processedUrls map[string]string
}

func NewCrawler() *Crawler { // constructor
	crawler := new(Crawler)
	crawler.processedUrls = make(map[string]string) // map to title

	return crawler
}

func worker(queue chan string, results chan CrawlerData, robot *Robot, done chan bool) {
	processed := make(map[string]bool)
	for {
		select {
		case <-done:
			return
		case url := <-queue:
			fmt.Println("len queue", len(queue))
			fmt.Println("len results", len(results))
			if robot.Disallow(url) {
				continue
			}
			robot.CrawlDelay()
			if url == "" {
				continue
			}
			body := download(url)
			if body == "" { // if url is not valid, move on
				continue
			}
			words, hrefs, title := extract(string(body))
			cleanedUrls := clean(url, hrefs)
			if cleanedUrls == nil { // invalid url, host is not usfca
				continue
			}
			data := CrawlerData{url, title, words, hrefs}
			results <- data
			for _, cleanedUrl := range cleanedUrls {
				_, ok := processed[cleanedUrl]
				if ok {
					continue // skip if we already visited site
				} else {
					fmt.Println("adding to queue", cleanedUrl) // introduces a concurrent read-map write situation
					processed[cleanedUrl] = true
					select {
					case queue <- cleanedUrl:
					case <-done:
						return
					}
				}
			}
		}

	}

}

type CrawlerData struct {
	url, title   string
	words, hrefs []string
}

func (crawler *Crawler) crawl(seedUrl string, ind SearchEngine) { // reciever function to crawl()
	robot := NewRobot()
	robot.ReadRecords(seedUrl)
	queue := make(chan string, 55000) // thread safe queue : main thread
	results := make(chan CrawlerData, 55000)
	done := make(chan bool)
	var wg sync.WaitGroup

	queue <- seedUrl // send the seed url to queue

	go worker(queue, results, robot, done)

	go func() { // the writing thread that processes the results
		for data := range results {
			if data.title == "" {
				data.title = data.url
			}
			ind.Insert(data.url, data.title, data.words) // have only one thread write to map and sqldb to prevent locks
		}
	}()
	go func() { // monitor that checks if we're done
		fmt.Println("hi")
		for {
			time.Sleep(5 * time.Second)
			fmt.Println("hey checking")               // check every five seconds to see if channels are empty
			if len(queue) == 0 && len(results) == 0 { // if empty for 5 seconds we close and quit
				fmt.Println("queue length", len(queue), "results length", len(results))
				close(queue)
				close(results)
				done <- true
				return
			}
		}
	}()
	wg.Wait()
	fmt.Println("Finished Crawling")
	<-done
}
