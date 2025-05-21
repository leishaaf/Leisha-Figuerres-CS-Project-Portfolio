import random
import csv

# Generate 100 random balances with IDs
num_balances = 100
data = []

for _ in range(num_balances):
    balance = round(random.uniform(0, 3010), 2)
    label = random.choice(['LME', 'Toler'])
    random_digits = ''.join(random.choices('0123456789', k=5))
    user_id = f"207{random_digits}"
    print(user_id, f"{balance:.2f}", label)

# Save to CSV file
with open('balances_with_ids.csv', 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerow(['ID', 'Balance', 'Label'])
    writer.writerows(data)

print("File 'balances_with_ids.csv' created successfully!")