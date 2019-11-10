from selenium import webdriver
import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
import selenium.webdriver.support.expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.keys import Keys
import time
from selenium.webdriver.common import action_chains, keys
from selenium import webdriver

'''lexis searcher'''


#def OpenBrowser():

'''def talk2Data():'''
'''need a function to interact with the data set to pull a press release/embargo
    date combo, one by one'''
'''feed each press release and date to the lexsearch'''


#add in the date argument after learning about the embargo data
def LexScrape(listOfLists):
    '''This segment of code will open the browser'''
    ''' Changed from Google Chrome to Firefox because I was having trouble
        with some button clicking on Google Chrome '''

    #google chrome path if I need it.

    '''path_to_chromedriver = '/home/john/Downloads/chromedriver'
    browser = webdriver.Chrome(executable_path = path_to_chromedriver)'''

    #Firefox
    browser = webdriver.Firefox()
    url = 'https://www.lexisnexis.com/hottopics/lnacademic/?verb=sf&amp;sfi=AC00NBGenSrch'
    browser.get(url)

    '''We will wait 30 seconds before throwing an exception'''

    browser.implicitly_wait(30)

    '''We will find the search bar and make sure it is cleared'''

    browser.switch_to_frame('mainFrame')
    browser.find_element_by_id('terms')
    browser.find_element_by_id('terms').clear()

    '''We will find all of the keywords from our press releases
    and search for them'''
    ''' We use the KeyWordExtract function to turn a list into a string seperated
    with commas to adhere to the searching format'''

    for key_features in listOfLists:

        search_terms = KeyWordExtract(key_features)
        #print(key_features)
        browser.find_element_by_id('terms').send_keys(key_features)

        ''' We go to the advanced options section to fill out the start and end date '''

        browser.find_element_by_xpath('//*[@id="advHeader"]/table/tbody/tr/td[2]/img').click()
        browser.switch_to.active_element
        browser.find_element_by_xpath('//*[@id="txtFrmDate"]').send_keys('1/1/2010')
        #we have to change the start date to the embargo date
        browser.find_element_by_xpath('//*[@id="txtToDate"]').send_keys('6/5/2016')
        #we have to change the end date to days that are x amount of days
        #past the embargo date
        browser.find_element_by_xpath("//*[@id='OkButt']").click()


        '''We press the 'submit' button '''

        browser.find_element_by_css_selector('input[type=\"submit\"]').click()
        browser.switch_to_default_content()

        '''Downloads the documents in a text file'''
        browser.switch_to_frame('mainFrame')


        #if (driver.find_elements_by_xpath("//*[contains(text(), 'No Documents Found')]") == True):
        dyn_frame = browser.find_elements_by_xpath('//frame[contains(@name, "fr_resultsNav")]')
        framename = dyn_frame[0].get_attribute('name')
        browser.switch_to_frame(framename)

        total = int(browser.find_element_by_name('totalDocsInResult').get_attribute('value'))

        if total > 500:
            initial = 1
            final = 500
            batch = 0
            while final <= total and final >= initial:
                batch+=1
                browser.find_element_by_css_selector('img[alt=\"Download Documents\"]').click()
                browser.switch_to_default_content()
                browser.switch_to_window(browser.window_handles[1])
                browser.find_element_by_xpath('//select[@id="delFmt"]/option[text()="Text"]').click()
                browser.find_element_by_css_selector('img[alt=\"Download\"]').click()
                results_url = browser.find_element_by_partial_link_text('.TXT').get_attribute('href')
                os.system('wget {}'.format(results_url))
                try:
                    element = WebDriverWait(browser, 120).until(EC.element_to_be_clickable((By.CSS_SELECTOR, 'img[alt=\"Close Window\"]')))
                except TimeoutException:
                    time.sleep(30)
                    browser.close()
                initial += 500
                if final + 500 > total:
                    final = total
                else:
                    final += 500
                backwindow = browser.window_handles[0]
                browser.switch_to_window(backwindow)
                browser.switch_to_default_content()
                browser.switch_to_frame('mainFrame')
                framelist = browser.find_elements_by_xpath('//frame[contains(@name, "fr_resultsNav")]')
                framename = framelist[0].get_attribute('name')
                browser.switch_to_frame(framename)

        browser.find_element_by_css_selector('img[alt=\"Download Documents\"]').click()
        browser.switch_to_window(browser.window_handles[1])
        browser.find_element_by_xpath('//select[@id="delFmt"]/option[text()="Text"]').click()
        browser.find_element_by_css_selector('img[alt=\"Download\"]').click()
        results_url = browser.find_element_by_partial_link_text('.TXT').get_attribute('href')
        os.system('wget {}'.format(results_url))
        ''' after I have downloaded, lets reset the search '''

        browser.find_element_by_xpath('//*[@id="restoreButtons"]/a[3]').click()

    else:
        pass
        print(key_features)



def KeyWordExtract(keyWord_List):
    myString = ''
    spacerCount = 0
    ''' this method is a helper method for LexSearch
    It will extract each element from the list'''
    #myString = " ".join(keyWord_List)
    ''' returns n a s a r o c k e t s '''
    for a in keyWord_List:
        b =  ''.join(str(x) for x in a)

        if spacerCount > 0:
            myString += ' ' + b
            spacerCount = spacerCount + 1
        else:
            myString += b
            spacerCount = spacerCount + 1

    ''' returns nasarockets '''
    return myString


#def LexSearch():
    '''for each item in press release:
            extract key words/embargo date
            LexScrape(keyWord_List, date)
            store it somewhere '''


def LexSearchTest(listOfLists):
    for x in listOfLists:
        LexScrape(x)
