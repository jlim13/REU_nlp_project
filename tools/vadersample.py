import nltk
import re
from nltk.tokenize import sent_tokenize
from vaderSentiment.vaderSentiment import sentiment as vaderSentiment
import glob
import linecache
import matplotlib.pyplot as newsplot
import matplotlib.pyplot as tweetplot
import numpy as np

def get_original(extension):
    original_path = "/home/john/Desktop/REU/finalassetreports/"
    z= "/home/john/Desktop/REU/title_and_urls/" + extension
    f = open(z, 'r')
    title = linecache.getline(z, 1)
    title = title.replace('\n', '')
    original_path= original_path+title
    fa = open(original_path, 'r')
    data = fa.read().replace('\n', '')

    return data

samplepath = "/home/john/Desktop/REU/title_and_urls/A 'super sensor' for cancer and CSIs.txt"
path = ("/home/john/Desktop/REU/title_and_urls/*.txt")
sentimentList = []
newscount = []
tweetcount = []

for fileName in glob.glob(path):

    extension = fileName.split('/')[6]
    data = get_original(extension)
    sent_tokenize_list = sent_tokenize(data)
    article_sentiment = 0

    with open (fileName) as fp:
        for line in fp:

            if line.startswith('-------'):
                newsLine = fp.next()
                tweetLine = fp.next()
                newsLine = int(newsLine[9:])
                tweetLine = int(tweetLine[10:])
        newscount.append(newsLine)
        tweetcount.append(tweetLine)

    for x in sent_tokenize_list:

        #print x,
        vs = vaderSentiment(x)
        #print "\n\t" + str(vs)
        #print(vs['compound'])
        article_sentiment = article_sentiment + vs['compound']
    article_sentiment = article_sentiment/(len(sent_tokenize_list))
    sentimentList.append(article_sentiment)
    print(article_sentiment, fileName.split('/')[6],newsLine, tweetLine)


print(len(newscount), len(sentimentList))

###sentiment x axis
###sucess measures y axis
##x , y

newsCoefficient = np.corrcoef(sentimentList, newscount)[0, 1]
tweetCoefficient = np.corrcoef(sentimentList, tweetcount)[0, 1]
print(newsCoefficient, tweetCoefficient)

'''
newsplot.plot( sentimentList,newscount, 'ro')
newsplot.title("Sentiment vs. News Success")
newsplot.ylabel('Number of News Articles')
newsplot.xlabel('Sentiment Score')
newsplot.axis([-1,1,0,30])
newsplot.show()

tweetplot.plot(sentimentList, tweetcount, 'ro')
tweetplot.title("Sentiment vs. Twitter Success")
tweetplot.ylabel('Number of Tweets')
tweetplot.xlabel('Sentiment Score')
tweetplot.axis([-1,1,0,30])

tweetplot.show()


'''
