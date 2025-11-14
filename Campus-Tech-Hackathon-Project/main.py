from flask import Flask, render_template, request, redirect, url_for, session
from datetime import datetime

import secrets

#commits 
app = Flask(__name__)
app.secret_key = '0ad399ba1c7e106f462428c079682c9c'

@app.route('/', methods=['GET', 'POST'])
def login():
    error = None

    if request.method == 'POST':
        user = request.form['username']
        pw = request.form['password']

        # Read stored credentials
        try:
            with open('users.txt', 'r') as file:
                users = [line.strip().split(',') for line in file]
        except FileNotFoundError:
            users = []

        # Check if user exists
        if [user, pw] in users:
            return redirect(url_for('student_action', username=user))
        else:
            # Register new user
            with open('users.txt', 'a') as file:
                file.write(f'{user},{pw}\n')
            return redirect(url_for('student_action', username=user))

    return render_template('login.html', error=error)

@app.route('/student-action/<username>', methods=['GET', 'POST'])
def student_action(username):
    balance = None
    building = None
    error = None

    if request.method == 'POST':
        student_id = request.form['student_id']
        action = request.form['action']

    # Read the balances.txt file and find the matching student ID
        try:
            with open('balances.txt', 'r') as file:
                for line in file:
                    parts = line.strip().split(' ')
                    if parts[0] == student_id:
                        balance = parts[1]
                        building = parts[2]
                        break
                else:
                    error = "Student ID not found."
        except FileNotFoundError:
            error = "balances.txt file not found."

        if error:
            return render_template('student_action.html', username=username, error=error)

        # Redirect to corresponding page, passing the necessary data
        if action == 'donate':
            return redirect(url_for('donate', username=username, student_id=student_id, balance=balance, building=building))
        elif action == 'request':
            return redirect(url_for('request_page', username=username, student_id=student_id, balance=balance, building=building))

    return render_template('student_action.html', username=username)
@app.route('/donation-failed')
def donation_failed():
    balance = request.args.get('balance',type=float)
    new_balance = float(balance) -  session['donation_total']
    donation_total = request.args.get('donation_total', 0, type=int)
    meal_count = request.args.get('meal_count', 0, type=int)
    snack_count = request.args.get('snack_count', 0, type=int)
    return render_template('donation_failed.html', donation_total=donation_total, balance=balance, new_balance=new_balance, meal_count=meal_count, snack_count=snack_count)


@app.route('/donation-success')
def donation_success():
    balance = request.args.get('balance',type=float)
    new_balance = float(balance) -  session['donation_total']
    donation_total = request.args.get('donation_total', 0, type=int)
    meal_count = request.args.get('meal_count', 0, type=int)
    snack_count = request.args.get('snack_count', 0, type=int)
    return render_template('donation_success.html', donation_total=donation_total, balance=balance, new_balance=new_balance, meal_count=meal_count, snack_count=snack_count)

@app.route('/donate/<username>/<student_id>/<balance>/<building>', methods=['GET', 'POST'])
def donate(username, student_id, balance, building):
    print("BALANCE " + balance)
    if 'donation_total' not in session:
        session['donation_total'] = 0
    # might need snack and meal counts but idk
    if 'meal_count' not in session:
        session['meal_count'] = 0
    if 'snack_count' not in session:
        session['snack_count'] = 0
    donation_amount = 0
    if request.method == 'POST':
        if 'meal' in request.form:
            session['meal_count'] += 1
            if donation_amount + 25 <= 50:
                session['donation_total'] += 25
            else:
                error = "Donation limit reached. You cannot donate more than $50."  
            
        elif 'snack' in request.form:
            session['snack_count'] += 1
            if donation_amount + 10 <= 50:
                session['donation_total'] += 10
            else:
                error = "Donation limit reached. You cannot donate more than $50."  
        elif 'reset' in request.form:
            session['donation_total'] = 0
            session['meal_count'] = 0
            session['snack_count'] = 0

        
        elif 'finish' in request.form:
            # check if user has enough first
            result = float(balance) - (session['donation_total'] + donation_amount)
            if(result < 1): # user isn't eligible to donate the amount they want
                return redirect(url_for('donation_failed', donation_total=session['donation_total'], meal_count=session['meal_count'],snack_count=session['snack_count'], balance=balance ))

            if session['donation_total'] + donation_amount > 50:
                error = "Donation limit reached. You cannot donate more than $50."     
                return render_template('donate.html', username=username, student_id=student_id, balance=balance,building=building,error=error)
            else:
                # valid donation amount 
                session['donation_total'] += donation_amount

                # with open bank_file


             #update the new balance

                
                return redirect(url_for('donation_success',donation_total=session['donation_total'],meal_count=session['meal_count'], snack_count=session['snack_count'],balance=balance))

                #create the students new updated balance and update it within balances.txt
                new_balance = float(balance) -  session['donation_total'] #update the new balance
                try:
                    with open('balances.txt', 'r') as file:
                        lines = file.readlines() # makes list of lines
                        for i, line in enumerate(lines): # loop through lines
                            parts = line.strip().split(' ')
                            if parts[0] == student_id: # find matching student id
                                parts[1] = str(new_balance)
                                lines[i] = ' '.join(parts) + '\n' # puts back together the updated part (id) and the rest of info
                                break
                    with open('balances.txt', 'w') as file:
                        file.writelines(lines)
                except FileNotFoundError:
                    error = "balances.txt file not found "
                
                # now write the result and added amount 
                try:
                    with open('bank.txt', 'r') as file: # write the previous lines from file 
                        lines = file.readlines()
                    # then added newly added entries
                    file = open("bank.txt", "w")
                    file.writelines(lines)
                    for i in range(session['meal_count']):
                        file.write("25" + "\n")
                    for i in range(session['snack_count']):
                        file.write("10" + "\n")
                        
                except FileNotFound:
                    error = "bank.txt file not found"
                return redirect(url_for('donation_success',donation_total=session['donation_total'],meal_count=session['meal_count'], snack_count=session['snack_count'],balance=balance, new_balance=new_balance))

    

    return render_template('donate.html', username=username, student_id=student_id, balance=balance,building=building)
   
 

@app.route('/request/<username>/<student_id>/<balance>/<building>', methods=['GET', 'POST'])
def request_page(username, student_id, balance, building):
    Toler_balance_check = {
        "January": 3010, "February": 2744, "March": 2060,
        "April": 1200, "May": 521, "June": 0, "July": 0,
        "August": 3010, "September": 2744, "October": 2060,
        "November": 1200, "December": 521
    }

    LME_balance_check = {
        "January": 2030, "February": 1776, "March": 1269,
        "April": 762, "May": 250, "June": 0, "July": 0,
        "August": 2030, "September": 1776, "October": 1269,
        "November": 762, "December": 250
    }

    current_month = datetime.now().strftime("%B")
    try:
        current_balance = float(balance)
    except ValueError:
        return f"Invalid balance format for student {student_id}."

    if building == "Toler":
        threshold = Toler_balance_check.get(current_month, 0)
    elif building == "LME":
        threshold = LME_balance_check.get(current_month, 0)
    else:
        return f"Unknown building: {building}"

    needs_flexi = current_balance < threshold
    queue_file = 'queue.txt'
    bank_file = 'bank.txt'
    error = None

    queue_file = 'queue.txt'
    success_message = None

    if request.method == 'POST' and needs_flexi:
        request_type = None
        request_amount = 0

        if 'meal' in request.form:
            request_type = 'meal'
            request_amount = 25
        elif 'snack' in request.form:
            request_type = 'snack'
            request_amount = 10
        else:
            error = "Please select an item to request."
            return render_template(
                'request.html',
                username=username,
                student_id=student_id,
                building=building,
                balance=current_balance,
                month=current_month,
                threshold=threshold,
                eligible=needs_flexi,
                error=error
            )
        


        # Log the request to the queue
        with open(queue_file, 'a') as f:
            f.write(f"{username},{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")


        # Update the bank file
        # updated_balance = current_balance - request_amount
        # new_lines = []

        # with open(bank_file, 'r') as f:
        #     for line in f:
        #         parts = line.strip().split(',')
        #         if len(parts) >= 4 and parts[0] == username and parts[1] == student_id:
        #             new_lines.append(f"{username},{student_id},{updated_balance},{building}\n")
        #         else:
        #             new_lines.append(line)

        # with open(bank_file, 'w') as f:
        #     f.writelines(new_lines)

        # ✅ Compute queue position
        # with open(queue_file, 'r') as f:
        #     queue_lines = f.readlines()
        #     position = next((i + 1 for i, line in enumerate(queue_lines) if line.startswith(username + ",")), None)
        position = get_queue_position(username)
    
        try:
            # Read the balances.txt file to find the student entry
            with open('balances.txt', 'r') as file:
                lines = file.readlines()

            balance_updated = False
            with open('balances.txt', 'w') as file:
                for line in lines:
                    # Split each line into student_id and balance
                    parts = line.strip().split()
                    current_student_id = parts[0]
                    current_student_balance = float(parts[1])

                    # If the student_id matches, update their balance
                    if current_student_id == student_id:
                        # Deduct the requested amount from the current balance
                        new_balance = current_student_balance + request_amount
                        balance_updated = True
                        # Write the updated balance to the file
                        file.write(f"{student_id} {new_balance:.2f} {building}\n")
                    else:
                        # Otherwise, write the line as is (for other students)
                        file.write(line)
        except FileNotFoundError:
            return render_template('request.html', error="balances.txt file not found.")

        new_balance=current_balance + request_amount

        try:
            with open(bank_file, 'r') as file:
                lines = file.readlines()

            balance_updated = False
            with open(bank_file, 'w') as file:
                for line in lines:
                    line = line.strip()
                    if line == str(request_amount) and not balance_updated:
                        balance_updated = True
                        continue  # Skip this line (remove one occurrence of the requested amount)
                    file.write(line + "\n")
        except FileNotFoundError:
            return render_template('request.html', error="bank.txt file not found.")


        return redirect(url_for('request_success',
                                username=username,
                                student_id=student_id,
                                old_balance=current_balance,
                                new_balance=new_balance,
                                item=request_type,
                                amount=request_amount,
                                position=position))


    return render_template('request.html',
                           username=username,
                           student_id=student_id,
                           building=building,
                           balance=current_balance,
                           month=current_month,
                           threshold=threshold,
                           eligible=needs_flexi,
                           error=error)


#add changes in the student balance

@app.route('/request-success')
def request_success():
    username = request.args.get('username')
    student_id = request.args.get('student_id')
    old_balance = float(request.args.get('old_balance'))
    new_balance = float(request.args.get('new_balance'))
    item = request.args.get('item')
    amount = request.args.get('amount')
    position = request.args.get('position')

    return render_template('request_success.html',
                           username=username,
                           student_id=student_id,
                           old_balance=old_balance,
                           new_balance=new_balance,
                           item=item,
                           amount=amount,
                           position=position)



@app.route('/request/<username>/<student_id>/<building>/<balance>/<eligible>/<queue_position>/<amount>')
def typage(username, student_id, building, balance, eligible, queue_position, amount):
    balance = float(balance)  # Use float for balance values
    queue_position = int(queue_position)
    amount = int(amount)

    # Read balances.txt entries and update balance for the given student ID
    with open('balances.txt', 'r') as f:
        balance_entries = [line.strip() for line in f if line.strip()]

    # Modify the balance for the given student ID
    updated_balance = None
    updated_lines = []
    for entry in balance_entries:
        student_data = entry.split()
        if student_data[0] == student_id:
            # Update balance for this student ID
            student_data[1] = str(balance + amount)
            updated_balance = balance + amount
        updated_lines.append(' '.join(student_data))

    # Write the updated balance entries back to the file
    with open('balances.txt', 'w') as f:
        for line in updated_lines:
            f.write(line + '\n')

    # Count how many requests before the current one have the same amount
    with open('queue.txt', 'r') as f:
        queue_lines = [line.strip() for line in f if line.strip()]

    # Get only those before this user in the queue
    prior_requests = queue_lines[:queue_position]

    # Count how many of them requested the same amount
    prior_same_amount = 0
    for line in prior_requests:
        user, timestamp = line.split(',', 1)
        prior_same_amount += 1

    # Debugging: Check how many prior requests were the same amount
    print(f"Prior same amount count: {prior_same_amount}")

    # Now let's check how many of the available items in the bank match the requested amount
    with open('bank.txt', 'r') as f:
        bank_entries = [line.strip() for line in f if line.strip()]

    available_same_amount = sum(1 for line in bank_entries if int(line.split()[1]) == amount)

    # Debugging: Check available items
    print(f"Available same amount count in bank: {available_same_amount}")

    # Check if the request can be fulfilled
    if prior_same_amount < available_same_amount:
        # Request can be fulfilled!
        status = f"✅ Your request for ${amount} has been fulfilled!"
    else:
        # Request not available yet
        status = f"⏳ Your request for ${amount} is still pending. Please wait."

    # Debugging: Output the status of the request
    print(status)

    # Return the updated page with balance and status
    return redirect(url_for('update',
                        username=username,
                        student_id=student_id,
                        building=building,
                        balance=updated_balance))


@app.route('/update/<username>/<student_id>/<building>/<balance>')
def update(username, student_id, building, balance):
     

    return render_template('update.html',
                           username=username, 
                           student_id=student_id, 
                           building=building, 
                           balance=balance)

@app.route('/welcome/<username>')
def welcome(username):
    return render_template('welcome.html', username=username)


def get_queue_position(username):
    queue_file = 'queue.txt'
    queue_position = None
    #hello
    #hii

    try:
        with open(queue_file, 'r') as f:
            queue_lines = f.readlines()

        # Loop through the queue lines and find the first occurrence of the username
        for position, line in enumerate(queue_lines, start=1):
            user, timestamp = line.strip().split(',')
            if user == username:
                queue_position = position
                break  # Once we find the username, we break out of the loop

        if queue_position is None:
            # If the username is not in the queue, assume they're in the first position
            queue_position = len(queue_lines) + 1

    except FileNotFoundError:
        queue_position = 1  # If the file doesn't exist, assume the first position

    return queue_position



# def send_email(username, subject, message_body):
#     sender_email = "cponti@dons.usfca.edu"
#     sender_password = "your_app_password"  # Use app password, not your real password
#     recipient_email = f"{username}@dons.usfca.edu"

#     msg = MIMEMultipart()
#     msg['From'] = sender_email
#     msg['To'] = recipient_email
#     msg['Subject'] = subject

#     msg.attach(MIMEText(message_body, 'plain'))

#     try:
#         # Connect to Gmail SMTP server
#         server = smtplib.SMTP('smtp.gmail.com', 587)
#         server.starttls()
#         server.login(sender_email, sender_password)

#         # Send the email
#         server.sendmail(sender_email, recipient_email, msg.as_string())
#         server.quit()
#         print("Email sent successfully to", recipient_email)
#         return True
#     except Exception as e:
#         print("Failed to send email:", e)
#         return False

if __name__ == '__main__':
    app.run(debug=True)