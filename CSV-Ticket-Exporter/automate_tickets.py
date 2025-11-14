from collections import defaultdict
import csv
import os
# file that reads csv files of the unaswered tickets queue and answered queue
# files are read and written to a combined csv file for historical reference
# while files are read, program collects the counts of the amount of tickets in each state and writes to a 
# result/performance csv file

def main():
    active = "active_tickets.csv"
    unanswered = "unanswered.csv"
    history = "historical_reference.csv"
    performance = "performance.csv"
    create_history(active, unanswered, history)
    state_counts, users = get_performance_data(history)
    create_performance(state_counts, users, performance)

def create_history(active, unanswered, history): # reads both csv files and writes to master csv that combines
    file_exists = os.path.isfile(history) and os.path.getsize(history) > 0 # checks if file is empty or exists to see if it's brand new or we append to existing history 
    with open(active, 'r', newline='', encoding='latin-1') as infile1, \
        open(unanswered, 'r', newline='', encoding='latin-1') as infile2, \
        open(history, 'a', newline='',  encoding='utf-8') as outfile:
        reader1 = csv.reader(infile1)
        reader2 = csv.reader(infile2)
        writer = csv.writer(outfile)
        if not file_exists: # if file doesn't exist write header
            writer.writerow(["number","state","u_username","description","short_description","priority","approval_set","assigned_to","assignment_group","sys_updated_on","sys_updated_by"]) # header 
        next(reader1, None) # ignore headers
        next(reader2, None)
        for row in reader1:
            writer.writerow(row) # write ticket into master csv file for history
        for row in reader2:
            writer.writerow(row)

def get_performance_data(history):
    with open(history, 'r', newline='',  encoding='utf-8') as file:
        reader = csv.reader(file)
        # make a dictionary that maps user to # of tickets they have for performance
        state_counts = defaultdict(int)
        users = defaultdict(int)
        next(reader, None)
        for row in reader:
            state = row[1]
            state_counts[state] += 1
            user = row[2] 
            users[user] += 1
    return state_counts, users

def create_performance(state_counts, users, performance):
     with open(performance, 'w', newline='',  encoding='utf-8') as outfile: # write to performance csv
        writer = csv.writer(outfile)
        writer.writerow(["metric", "value"]) # header
        for key, value in state_counts.items():
             writer.writerow([f"Tickets with state: {key}",value])
        for key, value in users.items():
            writer.writerow([f"Tickets submitted by: {key}",value])

if __name__ == "__main__":
    main()