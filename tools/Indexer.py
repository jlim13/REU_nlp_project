import glob
import os
import time
import datetime
import io

name = '/home/john/Desktop/REU/Tweets/rawtweets/*.txt'

class tweet:
    def __init__(self, t, u, w):
        self.t = t
        self.u = u
        self.w = w

for fileName in glob.glob(name):
    f = open(fileName, 'r')
    #go through each Tweet object
    #create one by one (DONT STORE EVERYTHIGN IN MEMO)
    #Check if a file with that date exists
    #if not create one
    #else go to the file:
    #write down the Tweet component of the object

    lines = []
    for line in f:
        line = line.strip()
        lines.append(line)
        if line.startswith("W"):
            theTweet = tweet(t=lines[-3], u=lines[-2], w=lines[-1])
            lines = []
            uglyDate = (theTweet.t).split()[1]
            newDate = datetime.datetime.strptime(uglyDate, '%Y-%m-%d').strftime('%m%d%y')
            #print(newDate)

            TweetMessage = (theTweet.w)[2:]


            theTweetDateFile = '/home/john/Desktop/REU/Tweets/processedTweets/'+newDate+'.txt'

            if(os.path.isfile(theTweetDateFile)):
                with io.FileIO(theTweetDateFile, 'a') as tweetFile:
                    tweetFile.write("\n")
                    tweetFile.write(TweetMessage)
            else:
                ##create a new file
                with io.FileIO(theTweetDateFile, 'w') as tweetFile:
                    tweetFile.write("\n")
                    tweetFile.write(TweetMessage)
            
