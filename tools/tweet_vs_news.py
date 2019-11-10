import glob
import matplotlib.pyplot as plt
import re
from scipy.stats.stats import pearsonr
import numpy

####################Shows Tweets vs. News articles for the press releases that pertain to only
##sensors/security

path = '/home/john/Desktop/REU/title_and_urls/*.txt'


tweetplotpoints = []
newsplotpoints = []

for fileName in glob.glob(path):


    with open(fileName) as fp:
        for line in fp:

            if line.startswith('-------'):
                newsLine = fp.next()
                tweetLine = fp.next()
                newsLine = int(newsLine[9:])
                tweetLine = int(tweetLine[10:])
        newsplotpoints.append(newsLine)
        tweetplotpoints.append(tweetLine)

'''
plt.plot(tweetplotpoints, newsplotpoints, 'ro')
plt.title("Twitter vs. News Articles Success")
plt.ylabel('Number of News Articles')
plt.xlabel('Number of Tweets')
plt.axis([0,30,0,30])
plt.show()'''
a = numpy.corrcoef(tweetplotpoints, newsplotpoints)[0, 1]
print  a
###############
