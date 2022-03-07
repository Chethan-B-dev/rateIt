import csv
num_attributes = 6
with open('c1.csv', 'r') as f:
    reader = csv.reader(f)
    your_list = list(reader)

h = ['0', '0', '0', '0', '0', '0']

for i in your_list:
    if i[-1] == "Y":
        j = 0
        for x in i:
            if x != "Y":
                if x != h[j] and h[j] == '0':
                    h[j] = x
                elif x != h[j] and h[j] != '0':
                    h[j] = '?'
                j = j + 1
    print(f"hypo is : {h}")
print("Most specific hypothesis is")
print(h)
