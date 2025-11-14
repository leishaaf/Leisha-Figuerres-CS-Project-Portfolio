package main

import (
	"fmt"
	"net/url"
	"strings"
)

func clean(host string, hrefs []string) []string {
	cleanedUrls := []string{}
	if host == "usfca.edu" {
		return nil
	}
	parsedHost, err := url.Parse(host)
	if err != nil {
		fmt.Println("ERROR READING HOST ", err)
		return nil
	}
	for _, href := range hrefs {
		url, err := url.Parse(href)
		if err != nil {
			fmt.Println("ERROR READING HREF ", err)
			continue
		}
		url.Fragment = ""   // remove all fragments
		if url.Host == "" { // checks if relative url
			cleanedUrl := parsedHost.ResolveReference(url)                   // found ResolveReference in go.dev - returns a new instance after resolving url reference to an absolute url
			if cleanedUrl.Scheme != "https" && cleanedUrl.Scheme != "http" { // skip unsupported scheme types
				continue
			}
			cleanedUrlStr := cleanedUrl.String()
			cleanedUrlStr = strings.TrimSpace(cleanedUrlStr)
			cleanedUrls = append(cleanedUrls, cleanedUrlStr)
		} else {
			if url.Host == parsedHost.Host && (url.Scheme == "https" || url.Scheme == "http") { // make sure we don't crawl to URLs that aren't hosted by our seed
				cleanedUrls = append(cleanedUrls, href)
			}

		}
	}
	return cleanedUrls
}

// no path but some fragment, take host path clear fragment
// if channel fills up wait five seconds
