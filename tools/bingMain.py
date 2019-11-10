import py_bing_search
from py_bing_search import PyBingNewsSearch
import time
import datetime
import sys
import os.path
import glob
import re
import os
import linecache

def get_news_articles(search_terms, date, fileName ):
    bing_news = PyBingNewsSearch('q8VQn8X56yeuUuBFdoA8NKiVULvZV+RzgcJVUX60MSk', search_terms)
    first_fifty_results = bing_news.search(limit = 5000, format = 'json')
    for news in first_fifty_results:
        #if the date is out of a certain range...
    	#cut everything from 't' on
    	date = news.date
    	date = date[:date.rfind('T')]
    	#new date format is yyyy-mm-dd
    	d = datetime.datetime.strptime(date, "%Y-%m-%d")
    	newDate = datetime.date.strftime(d, "%m%d%y")
    	toBeComparedDate = datetime.datetime.strptime(newDate, "%m%d%y")
    	print(toBeComparedDate)
    	## new date is in the format 062116 or mmddyy
    	date1 = '060109'
    	date2 = '123109'
    	minDate = datetime.datetime.strptime(date1,  '%m%d%y')
    	maxDate = datetime.datetime.strptime(date2, '%m%d%y')

        if(toBeComparedDate < maxDate and toBeComparedDate > minDate):
            print(news.url)
            newsList = []
            newsList.append(news.url)
            f = open(fileName, 'a')
            f.write('\n'.join(newsList))
            f.write("\n")



path = '/home/john/Desktop/REU/2000reports/*.txt'
for fileName in glob.glob(path):
    f = open(fileName, 'r')
    first_line = f.readline()
    second_line = f.next()
    third_line = linecache.getline(fileName, 3)
    keyword = ': '
    regexp = re.compile(":(.*)$")
    search_terms = regexp.search(second_line).group(1)
    date = regexp.search(third_line).group(1)
    search_terms = search_terms[1:]
    date = date[1:]
    get_news_articles(search_terms, date, fileName)
