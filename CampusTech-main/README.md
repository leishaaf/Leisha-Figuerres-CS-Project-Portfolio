# CampusTech

# 🧚‍♀️ Be a Flexi Fairy (BFF) – DonsHack25

Flexi Fairy is a Flask web application built to address **food insecurity on campus** by connecting students who have **excess flexi dollars** with those who **need meals or snacks**. 

Built during **DonsHack25**, this project empowers the USF community to give and receive within a trusted student-only platform.

---

## 🚀 Features

- **Login System:** Students log in using their Student ID, username, and password.
- **Donate or Request Meals/Snacks:**
  - Donate up to **$50/day** in flexi dollars.
  - Request meals/snacks on a **first-come, first-served** basis.
  - Priority Queue system for requests.
- **Meal Tracking:**
  - $10 = Snack
  - $25 = Meal
- **Success and Error Pages:** 
  - Donation confirmations with animated graphics (Fairy GIF, confetti).
  - Invalid input messages with explanations.
- **Data Logging:**
  - Transactions logged into `bank.txt` and `queue.txt`.

---

## 💻 Tech Stack

- **Backend:** Python, Flask
- **Frontend:** HTML, CSS (Optima font), JavaScript
- **Database:** Text file-based system (`balances.txt`, `bank.txt`, `queue.txt`)
- **GET Mobile:** Optional linking via [`https://get.cbord.com/usfca/full/prelogin.php`](https://get.cbord.com/usfca/full/prelogin.php)

---

## 🔁 User Flow

### Page 1: Login
- Input: Username, Password, Student ID
- Authenticates student session.

### Page 2: Choose Action
- Options: **Donate** or **Request**
- View current balance and eligibility chart.

### Page 3: Request a Meal/Snack
- Check flexi balance (must be under threshold).
- Choose to request Snack ($10) or Meal ($25).
- Limit: **1 request/day**, **3 requests/week**
- Confirmation via email.

### Page 4: Donate a Meal/Snack
- Select Snack ($10) or Meal ($25) – max $50/day.
- Students press a button however many times they'd like to donate
- Checks if student is eligible to donate (if student has enough to donate the amount accumulated) before processing
- Balance updates and donor receives thank-you message.
- If student is ineligible they're informed & get the chance to go back and re-enter a different amount

### Page 5: Final Pages
- Success: Confetti, GIFs, and message of appreciation.
- Fail: Explanation of why request/donation failed and link to try again.

---
