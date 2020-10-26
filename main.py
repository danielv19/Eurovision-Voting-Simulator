from ESC_setup import load_countries, participant_setup, setup_vote, vote
##allpossible[38]-[41] are recent withdrawn countries, [47]-[50] are withdrawn countries that are somewhat obscure
load_countries()
print("Welcome to the Eurovision Grand Final")
print("Please Type The Other 21 Countries That Will Join The Big 5 In The Final")
rando = input("Do You Want A Random Selection? y/n: ")
#1D Array of Participants
finalist = participant_setup(rando)
##Laying out voting system
## "voting" are the countries giving out points
print("The 'Old' voting system is one that only countries that made it to the final can vote")
print("The 'Modern' voting system is one that all participating countries can vote")
pref = input("Which voting style would you like? Old or Modern? ")
#1D Array of Voters
voting = setup_vote(pref, finalist)
#points_finalist where points can be tallied
points_finalist = {}
for i in range(26):
  points_finalist[finalist[i]] = 0
#Setup Point Giving
vote(points_finalist, finalist, voting)
