import glob
import fuzzywuzzy
import re
from fuzzywuzzy import fuzz
from fuzzywuzzy import process
import linecache
import os
import time
#path = '/home/john/Desktop/REU/title_and_urls/*.txt'
path = '/home/john/Desktop/REU/title_and_urls/A flash of light turns graphene into a biosensor.txt'


def search(search_terms, fileName):
    f = open(fileName, 'a')
    f.write('--------Tweets--------')
    f.write("\n")
    a = search_terms

    for tweetlist in glob.glob('/home/john/Desktop/REU/Tweets/*.txt'):
        with open (tweetlist, 'r') as f1:
            f.write("Tweets::")
            f.write("\n")
            for line in f1:
                if line.startswith('W	'):
                    b = line
                    ratio = fuzz.token_set_ratio(a,b)
                    if ratio > 70:
                        f.write(b)
                        f.write("\n")
                    else:
                        print(ratio)




for fileName in glob.glob(path):
    f = open(fileName, 'r')
    base = os.path.basename(fileName)
    name = os.path.splitext(base)[0]
    first_line = f.readline()
    second_line = f.next()
    third_line = linecache.getline(fileName, 3)
    keyword = ': '
    regexp = re.compile(":(.*)$")
    date = regexp.search(third_line).group(1)
    date = date[1:]
    month = date[:2]
    start = time.time()
    search(name, fileName)
    end = time.time()
    print(start)
    print(end)
    print(end-start)

