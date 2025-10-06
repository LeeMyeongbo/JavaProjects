from gtts import gTTS
import re

text = ''
file = open('result.txt', 'r', encoding='UTF8')
for line in file:
    text += (line + '.\n')
file.close()

cleaned_text = re.sub(r'[#▲△▶▷▼▽◀◁^@·]', ' ', text)
cleaned_text = re.sub(r'\s+', ' ', cleaned_text).strip()

if cleaned_text == '':
    cleaned_text = '검색된 뉴스가 없습니다.'

tts = gTTS(text=cleaned_text, lang='ko')
tts.save("news.mp3")
