import mailbox

mbox = mailbox.mbox('C:/Users/RosaAbrahamsson/Downloads/All mail Including Spam and Trash.mbox')
nEmails = 0

for message in mbox:
    print(f"From: {message['from']}")
    print(f"To: {message['to']}")
    print(f"Subject: {message['subject']}")
    print(f"Date: {message['date']}")
    #print(f"Body: {message.get_payload()}")
    print("-" * 40)
    message
    nEmails += 1

print(nEmails)
