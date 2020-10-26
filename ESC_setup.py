import random
from collections import Counter
allpossible = []
voting = []
#loads countries that can participate
def load_countries():
  fiile = open("allcountries.txt", "r")
  for countries in fiile:
    allpossible.append(countries.strip())
  print("COUNTRIES HAVE BEEN LOADED")
  fiile.close()
#sets up which countries will compete in the grand final
# either by a random draw or manually
# paramter is answer_random which will determine whether to randomly generate participants or manually
def participant_setup(answer_random):
  finalist = ["GERMANY", "ITALY", "SPAIN", "UNITED KINGDOM", "FRANCE"]
  #random
  if answer_random.lower() == "y" or answer_random.lower() == "yes":
    for i in range(21):
      stay = True
      while stay:
        false = 0
        num = random.randint(1, 41)
        for j in range(len(finalist)):
          if allpossible[num].upper() == finalist[j]:
            false = 1
        if false == 0:
          finalist.append(allpossible[num].upper())
          stay = False
    for i in range(26):
      print(f"{i+1}: {finalist[i]}")
  else:
    ##get all finalists
    i = 0
    while i < 21:
      n = input(f"{i+1}: ").strip().upper()
      exit = False
      ##Check if country is applicable
      while exit == False:
        for j in range(49):
          target = allpossible[j].upper()
          if n == target and (n != "GERMANY" or n != "ITALY" or n != "SPAIN" or n != "FRANCE" or n != "UNITED KINGDOM"):
            ##check for doubles
            for countries in finalist:
              if countries.upper() == n.upper():
                leave = False
                while leave == False:
                  n = input("That Country Has Already Been Selected Or Not Applicable: ").strip().upper()
                  gnmsnt = 0
                  checkp = 0
                  for q in range(len(finalist)):
                    for j in range(49):
                        target = allpossible[j].upper()
                        if n == finalist[q]:
                          gnmsnt = 1
                        elif gnmsnt != 1 and n == target and (n != "GERMANY" or n != "ITALY" or n != "SPAIN" or n != "FRANCE" or n != "UNITED KINGDOM"):
                          gnmsnt = 2
                  if gnmsnt == 0 or gnmsnt == 2:
                    leave = True
                  elif gnmsnt != 0:
                    leave = False
            exit = True
            break
        if exit != True:
          n = input("(Country is mispelled or not applicable) Enter Another One: ").strip().upper()
      finalist.append(n)
      i += 1
    print("Along with the Big 5")
    i = 0
    for i in range(5):
      print(f"{22+i}: {finalist[i]}")
  return finalist

#sets voting to old or modern
def setup_vote(version, finalists):
  if version.lower().strip() == "old":
    voting = finalists
  elif version.lower().strip() == "modern":
    voting = allpossible[:46]
  return voting

def greeting(country):
  greeting = random.randint(0,2)
  if country == "FRANCE" or country == "LUXEMBOURGH" or country == " BELGIUM" or country == "MONACO" or country == "ANDORRA" or country == "SWITZERLAND":
    print(f"Bonsoir Europe, voici nos points de {country}")
  elif greeting == 0:
    print(f"Hello Europe, {country} calling! Here are our points!")
  elif greeting == 1:
    print(f"Good Evening Europe! This is how {country} voted!")
  elif greeting == 2:
    print(f"{country} calling! What a great show, these are our points.")

def print_top10(boolean, liste):
  #print the top 10
  if boolean:
    counter = Counter(liste)
    top10 = counter.most_common(10)
    print("-------------------")
    print("The Top 10 So Far")
    place = 1
    for i in top10:
      if place < 10:
        print(f" {place}.| {i[0]} : {i[1]}")
      else:
        print(f"{place}.| {i[0]} : {i[1]}")
      place += 1
    print("-------------------")

def print_autovote(country, liste):
  print("-------------------")
  print(f"{country}'s votes:")
  for i in reversed(range(10)):
    if i < 8:
      print(f" {i+1}| {liste[i]}")
    elif i == 8:
      print(f"{i+2}| {liste[i]}")
    elif i == 9:
      print(f"{i+3}| {liste[i]}")

def vote(point_counter, finalists, voting_countries):
  showTop10 = False
  for country in voting_countries:
    print_top10(showTop10, point_counter)
    #print a greeting 
    greeting(country)
    recieved = []
    rando2 = input("Would you like to randomly generate points? y/n: ")
    i = 0
    if rando2 == "yes" or "y":
      for i in range(10):
        stay = True
        while stay:
          false = 0
          num = random.randint(0, 25)
          for j in range(len(recieved)):
            if finalists[num] == recieved[j] or finalists[num] == country:
              false = 1
          if false == 0:
            recieved.append(finalists[num])
            stay = False
            if i < 8:
              point_counter[finalists[num]] += i + 1
            elif i == 8:
              point_counter[finalists[num]] += i + 2
            elif i == 9:
              point_counter[finalists[num]] += i + 3
            stay = False
      print_autovote(country, recieved)
      showTop10 = True
    else:  
      while i < 12:
        if i == 8:
          i = 9
        elif i == 10:
          i = 11
        poang = input(f"{i+1} point: ").strip().upper()
        confirmed = False
          ##Check if country is applicable
        while confirmed == False:
          for j in range(len(finalists)):
            target = finalists[j].upper()
            if poang == country:
              while poang == country:
                poang = input("You Cannot Vote For Yourown Country: ").strip().upper()
            if poang == target:
              for already in recieved:
                if poang == already:
                  while poang == already:
                    elder = poang
                    print(f"Sorry, you already gave {elder} points")
                    poang = input(f"You Cannot Vote For {elder} twice: ").strip().upper()
              confirmed = True
          if confirmed != True:
            old = poang
            poang = input(f"Sorry {old} is not in the final: ").strip().upper()
        recieved.append(poang)
        point_counter[poang] += i + 1
        i += 1
        showTop10 = True
