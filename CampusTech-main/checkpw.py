import random
import string
import time

def contains_username(username, password):
    password_len = len(password)
    username_len = len(username)
    
    # Check if username can be contained in password
    if username_len > password_len:
        return False
    
    for i in range(password_len - username_len + 1):
        for j in range(username_len):
            if password[i + j].lower() != username[j].lower():
                break
            # If the end of username was reached, there was a match
            if j == username_len - 1:
                return True
    return False

def is_strong_password(username, password):
    letter_count = 0
    has_upper = False
    has_lower = False
    has_digit = False
    has_string = False
    has_username = False
    
    # Check length requirement
    if len(password) < 8:
        return False
    
    # Check if password contains username
    if contains_username(username, password):
        has_username = True
    
    # Check password characters
    for ch in password:
        # Check if character is alphanumeric
        if not ch.isalnum():
            return False
            
        if ch.isupper():
            has_upper = True
        if ch.islower():
            has_lower = True
        if ch.isdigit():
            has_digit = True
        if ch.isalpha():
            letter_count += 1
            if letter_count >= 4:
                has_string = True
        else:
            letter_count = 0
    
    return has_upper and has_lower and has_digit and has_string and not has_username

def is_strong_default_password(username, password):
    has_upper = False
    has_lower = False
    has_digit = False
    has_username = False
    
    # Length requirement
    if len(password) < 8 or len(password) > 15:
        return False
    
    # Check if password contains username
    if contains_username(username, password):
        has_username = True
    
    # Check password characters
    for ch in password:
        if not ch.isalnum():
            return False
        if ch.isupper():
            has_upper = True
        if ch.islower():
            has_lower = True
        if ch.isdigit():
            has_digit = True
    
    return has_upper and has_lower and has_digit and not has_username

def generate_default_password(username):
    uppers = string.ascii_uppercase
    lowers = string.ascii_lowercase
    digits = string.digits
    
    while True:
        # Generate random length between 8 and 15
        password_len = random.randint(8, 15)
        default_password = ""
        
        # Generate random password
        for _ in range(password_len):
            char_type = random.randint(0, 2)
            if char_type == 0:
                default_password += random.choice(uppers)
            elif char_type == 1:
                default_password += random.choice(lowers)
            else:
                default_password += random.choice(digits)
        
        if is_strong_default_password(username, default_password):
            print(default_password)
            return default_password

def main():
    # Seed random number generator
    random.seed(time.time())
    
    while True:
        username = input("Enter username: ")
        password = input("Enter new password: ")
        
        if not is_strong_password(username, password):
            print("Your password is weak. Try again!\n")
            print("Generating a default password...")
            print("Generated default password: ", end="")
            generate_default_password(username)
            break
        else:
            print("Strong password!\n")
            break

if __name__ == "__main__":
    main()