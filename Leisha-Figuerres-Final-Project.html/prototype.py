#Cs 110
#Leisha Figuerres
#Final Project Code

#Login information just in case:
#User: 31ax65qhrhpg24ltaa6nhhmzqk2u
#Password: Usfca2023

from spotipy.oauth2 import SpotifyClientCredentials
from fuzzywuzzy import fuzz
import spotipy
import sys
from flask import Flask, session,request,redirect, url_for
import secrets

app = Flask(__name__)
app.debug = True

#Home page where user input's an artist
@app.route('/')
def form_example():
    html = ''
    html += '<html>\n'
    html += '<head>'
    html += '<style>'
    html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
    html += '</style>'
    html += '</head>'
    html += '<body>\n'
    html += '<center>'
    html += '<h3>Welcome to...</h3>'
    html += '<h1>Guess the Top Song of the Artist</h1>'
    html += '<br>'
    html += '<form method="POST" action="form_input">\n'
    html += 'Enter a verified artist on Spotifiy: <input type="text" name="artist_inputted" />\n'
    html += '<p>\n'
    html += '<input type="submit" value="Submit" />\n'
    html += '</center>'
    html += '</form>\n'
    html += '</body>\n'
    html += '</html>\n'
    return html

#Guess route where user's input their guess and redirects to display route where answer is tested
@app.route('/guess', methods=['GET'])
def guess():
    artist = session.get('artist_name')
    html = ''
    html += '<html>\n'
    html += '<head>'
    html += '<style>'
    html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
    html += '</style>'
    html += '</head>'
    html += '<body>\n'
    html += '<center>'
    html += '<form method="POST" action="display">\n'
    html += '<h2>Guess the most popular song of the artist, '+ artist +'</h2>'
    html += 'Enter guess: <input type="text" name="guess" placeholder="Dont forget features!"/>\n'
    html += '<input type="submit" value="submit" />\n'
    html += '</form>\n'
    html += '<p>'
    html += 'If guess is not working, try including <strong>artists that might be featured</strong> <br> on the top track or any <strong>additional information</strong> about the track<br>in parenthesis or brackets included in the song title!'
    html += '</p>'
    html += '<p>'
    html += '<nav>'
    html += '<a href="/">Back</a>'
    html += '</nav>'
    html += '</p>'
    html += '</center>'
    html += '</body>\n'
    html += '</html>\n'
    return html
    return redirect(url_for('display'))

#Reveal page that will display if user gets a guess incorrect and want's to display the answer along with information
#Same code as display route if guess was correct with a few changes
@app.route('/reveal', methods=['GET', 'POST'])
def reveal():
  try:
    #Determines the top song of the artist by searching for their top songs and putting those songs into a list based on popularity
    #The song at index 0 is the top song

    sp = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials(client_id="5d27f13aa9dc43a4b7b18ce72dd78d1c",
                                                                 client_secret="825417a477d14ae79953f3038c9b9255"))
    artID = session.get('artID')
    if len(sys.argv) > 1:
      urn = sys.argv[1]
    else:
        urn = artID

    response = sp.artist_top_tracks(urn)  #Searches for top track
    artist_info = sp.artist(urn)
    artist = artist_info['name']  #Variable assigned to the official spotify name user searched for to be used when displaying information in other routes

    tracks_list = []

    for track in response['tracks']:
      track_name = (track['name'])
      tracks_list.append(track_name)

    top_track = tracks_list[0] #Assigns top track to variable

    html = ''
    html += '<html>\n'
    html += '<head>'
    html += '<style>'
    html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
    html += '</style>'
    html += '</head>'
    html += '<body>\n'
    html += '<center>'
    html += '<h1>'
    html += 'The top song of ' + artist + ' is: ' + top_track 
    html += '</h1>'
    html += '</center>'
    html += '<p>'
    html += 'Were you even close?...'
    html += '<ol>'
    #Displays the list of top ten tracks in ordered list
    for index in range(len(tracks_list)):
      html += '<li>' + tracks_list[index] + '</li>'
    html += '</ol>'
    html += '</p>'

    #Parses data about the top track using spotify's search and get method
    for track in response['tracks']:
      track_name = (track['name'])
      if track_name == top_track:
        track_urls = str(track.get("external_urls"))
        track_urls = track_urls.strip("{}").strip("'").strip(":").strip("'spotify").strip().strip(" :").strip(" '")
        track_displayedID = str(track.get("id"))
        track_popularity = str(track.get("popularity"))
        track_num = str(track.get("track_number"))
        track_type = str(track.get("type"))

    html += '<h3>'
    html += 'Information about ' + artist + 's ' + ' top track: ' + top_track
    html += '</h3>'
    html += '<p>'
    html += 'Track urls: ' + '<a target="_blank" rel="noopener" href='+track_urls+'>Play Top Track</a>' + '<br>'
    html += 'Track ID: ' + track_displayedID + '<br>'
    html += 'Track Popularity: ' + track_popularity + '<br>'
    html += 'Track Number on Album: ' + track_num + '<br>'
    html += 'Track Type: ' + track_type 
    html += '</p>'

    for track in response['tracks']:
      track_name = (track['name'])
      if track_name == top_track:
        track_album = track.get("album")
        for item in track_album:
          albumID = track_album.get("uri")

    #Parses data about the top track's album using spotify's search and get method
    #Get's album ID and retrieves data using it
    html += '<h3>Information about the album that the top track, ' + top_track + ', comes from:</h3>'
    if len(sys.argv) > 1:
      urn3 = sys.argv[1]
    else:
        urn3 = albumID

    album = sp.album(urn3)
    for item in album:
      track_amount = str(album.get("total_tracks"))
      album_name = str(album.get("name"))
      album_id = str(album.get("id"))
      album_release = str(album.get("release_date"))
    html += '<p>'
    html += 'Album Name: ' + album_name + '<br>'
    html += 'Album Number of Tracks: ' + track_amount + '<br>'
    html += 'Album ID: ' + album_id + '<br>'
    html += 'Album Release Date: ' + album_release + '<br>'
    html += '</p>'
    html += '<nav>'
    html += '<a href="/">Back to Play Again!</a>'
    html += '</nav>'
    html += '</body>\n'
    html += '</html>\n'
    return html
  except:
    html = ''
    html += '<html>\n'
    html += '<body>\n'
    html += '<h2> Unexpected Error </h2>'
    html += '<p> This error might have occured because there was a problem with the API retrieving data'
    html += '<nav>'
    html += '<a href="/">Back to homepage</a>'
    html += '</nav>'
    html += '</body>\n'
    html += '</html>\n'

#Route that get's artist ID from searchArtist function and determines if user's guess is correct or not
@app.route('/display', methods=['GET', 'POST'])
def display():
  try: 
    #Determines the top song of the artist by searching for their top songs and putting those songs into a list based on popularity
    #The song at index 0 is the top song
    sp = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials(client_id="5d27f13aa9dc43a4b7b18ce72dd78d1c",
                                                                 client_secret="825417a477d14ae79953f3038c9b9255"))
    artID = session.get('artID')
    if len(sys.argv) > 1:
      urn = sys.argv[1]
    else:
        urn = artID

    response = sp.artist_top_tracks(urn) #Searches for top track
    artist_info = sp.artist(urn)
    artist = artist_info['name']
    #Variable assigned to the official spotify name user searched for to be used when displaying information in other routes
    session['artist'] = artist
    tracks_list = []

    for track in response['tracks']: 
      track_name = (track['name'])
      tracks_list.append(track_name)

    top_track = tracks_list[0] #Assigns top track to variable

    guess = request.form['guess']

    #Tests the similarity between user inputted guess and the official title of the top track to determine if guess is correct or not
    similarity = fuzz.ratio(guess.lower(), top_track.lower())
    #If guess is correct, top ten tracks of artist, information about the top track, and the album is from is displayed
    if similarity > 70:
      sp = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials(client_id="5d27f13aa9dc43a4b7b18ce72dd78d1c",
                                                                 client_secret="825417a477d14ae79953f3038c9b9255"))
      html = ''
      html += '<html>\n'
      html += '<head>'
      html += '<style>'
      html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
      html += '</style>'
      html += '</head>'
      html += '<center>'
      html += '<body>\n'
      html += '<h1>'
      html += 'The top song of ' + artist + ' is: ' + top_track 
      html += '</h1>'
      html += '<h2>You Guessed Correctly!</h2>'
      html += '</center>'
      html += '<p>'
      html += '<h3>Top Ten Track List:</h3>'
      html += '<ol>'
      #Displays the list of top ten tracks in ordered list
      for index in range(len(tracks_list)):
        html += '<li>' + tracks_list[index] + '</li>'
      html += '</ol>'
      html += '</p>'

      #Parses data about the top track using spotify's search and get method
      for track in response['tracks']:
        track_name = (track['name'])
        if track_name == top_track:
          track_urls = str(track.get("external_urls"))
          track_urls = track_urls.strip("{}").strip("'").strip(":").strip("'spotify").strip().strip(" :").strip(" '")
          track_displayedID = str(track.get("id"))
          track_popularity = str(track.get("popularity"))
          track_num = str(track.get("track_number"))
          track_type = str(track.get("type"))

      html += '<h3>'
      html += 'Information about ' + artist + 's ' + ' top track: ' + top_track
      html += '</h3>'
      html += '<p>'
      html += 'Track urls: ' + '<a target="_blank" rel="noopener" href='+track_urls+'>Play Top Track</a>' + '<br>'
      html += 'Track ID: ' + track_displayedID + '<br>'
      html += 'Track Popularity: ' + track_popularity + '<br>'
      html += 'Track Number on Album: ' + track_num + '<br>'
      html += 'Track Type: ' + track_type 
      html += '</p>'
      html += '<p>'
      for track in response['tracks']:
        track_name = (track['name'])
        if track_name == top_track:
          track_album = track.get("album")
          for item in track_album:
            albumID = track_album.get("uri")

    #Parses data about the top track's album using spotify's search and get method
    #Get's album ID and retrieves data using it
      html += '<h3>Information about album that the top track, ' + top_track + ', comes from:</h3>'
      if len(sys.argv) > 1:
        urn3 = sys.argv[1]
      else:
          urn3 = albumID

      album = sp.album(urn3)
      for item in album:
        track_amount = str(album.get("total_tracks"))
        album_name = str(album.get("name"))
        album_id = str(album.get("id"))
        album_release = str(album.get("release_date"))

      html += 'Album Name: ' + album_name + '<br>'
      html += 'Album Number of Tracks: ' + track_amount + '<br>'
      html += 'Album ID: ' + album_id + '<br>'
      html += 'Album Release Date: ' + album_release + '<br>'
      html += '</p>'
      html += '<nav>'
      html += '<a href="/">Back to Play Again!</a>'
      html += '</nav>'
      html += '</body>\n'
      html += '</html>\n'
      return html
    else:
      #Display's if guess isn't correct and gives options to restart completely, guess again, or reveal the answer
      html = ''
      html += '<html>\n'
      html += '<head>'
      html += '<style>'
      html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
      html += '</style>'
      html += '</head>'
      html += '<body>\n'
      html += '<p>'
      html += '<center>'
      html += '<h2>'
      html += 'Your guess was incorrect...'
      html += '</h2>'
      html += '</p>'
      html += '<nav>'
      html += '<a href="/">Back to Play Again/Restart!</a>'
      html += '</nav>'
      html += '<nav>'
      html += '<a href='+ url_for('guess')+ '>Guess Again</a>'
      html += '</nav>'
      html += '<a href='+ url_for('reveal')+'>Reveal the answer... :(</a>'
      html += '</body>\n'
      html += '<center>'
      html += '</html>\n'
      return html
  except:
    #Unexpected error handling 
    html = ''
    html += '<html>\n'
    html += '<body>\n'
    html += '<h2> Unexpected Error </h2>'
    html += '<p> This error might have occured because there was a problem with the API retrieving data'
    html += '<nav>'
    html += '<a href="/">Back to homepage</a>'
    html += '</nav>'
    html += '</body>\n'
    html += '</html>\n'

#Function that displays a screen based on if an artist was found or not
#If not found, will display if statement's first clause, if it is found user is redirected to the guessing page
@app.route('/form_input', methods=['POST'])
def form_input():
  artist_inputted = request.form['artist_inputted']
  results1 = searchArtist(artist_inputted)
  if results1 == None:
    html = ''
    html += '<html>\n'
    html += '<head>'
    html += '<style>'
    html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
    html += '</style>'
    html += '</head>'
    html += '<body>\n'
    html += '<center>'
    html += '<p>'
    html += '<h2>'
    html += 'The artist you searched for is not in the spotify database.'
    html += '</h2>'
    html += '</p>'
    html += '<nav>'
    html += '<a href="/">Back to Play Again!</a>'
    html += '</nav>'
    html += '</center>'
    html += '</body>\n'
    html += '</html>\n'
    return html
  else:
    artID = results1[0]
    session['artID'] = artID 
    artist = results1[1]
    html = ''
    html += '<html>\n'
    html += '<head>'
    html += '<style>'
    html += 'body {background: linear-gradient(to right, rgb(227, 231, 160), rgb(233, 237, 201), rgb(231, 241, 220));font-family: "Domine ", serif;font-size: 1.2em;color: rgb(83, 89, 70)}\n'
    html += '</style>'
    html += '</head>'
    html += '<body>\n'
    html += artist + ' was found!' '\n'
    sp = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials(client_id="5d27f13aa9dc43a4b7b18ce72dd78d1c",
                                                               client_secret="825417a477d14ae79953f3038c9b9255"))
    html += '</body>\n'
    html += '</body>\n'
    html += '</html>\n'
    return redirect(url_for('guess'))


#Function that searches for artist based on what the user inputted
def searchArtist(artist_searched):    
  artist_searched = artist_searched.strip().lower()

  sp = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials(client_id="5d27f13aa9dc43a4b7b18ce72dd78d1c",
                                                             client_secret="825417a477d14ae79953f3038c9b9255"))

  artist_info = sp.search(q=artist_searched.lower(), type='artist')
  #If artist exists, their artist ID is retrieved and is sent to display route
  found = False
  for artist in artist_info['artists']['items']: 
  #Uses fuzzy search to determine similarity ratio, meaning users can mispell artists name and program will still find correct artist as long as it's similar
    similarity = fuzz.ratio(artist.get("name").lower(), artist_searched.lower())
    if similarity > 70:
      artist_id = artist.get("id")
      session['artist_name'] = artist.get("name")
      return artist_id, artist.get("name")
      found = True
      break
  #Flag that tests if the artist doesn't exist
  if found == False:
      return None 

if __name__ == '__main__':
  #To secure session
  app.secret_key = secrets.token_hex(16)
  app.run()




