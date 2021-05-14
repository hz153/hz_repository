# -*- coding:utf-8 -*-
import requests
import calendar
import json
import time
import csv
import re
# TODO 全局变量，需要选择其他股票可以更改query
query = "长城汽车"
user = "9532739010"
sort_type = "time"
access_token = "1f69696036a1f835311038792cefd029700a78c1"
per_count = "20"
sort_id = "2"


def store_csv(list_):
    fp = open(r'C:\Users\10155\Desktop\stock_comment.csv', 'a', encoding='gbk', newline='')
    csv_writer1 = csv.writer(fp)
    csv_writer1.writerow(list_)
    fp.close()


def process_text(text):
    text = re.sub(r'\$.+?\$', '', text)
    text = re.sub(r'#.+?#', '', text)
    text = re.sub(r"[【】]", '', text)
    text = re.sub(r"\|", '', text)
    text = re.sub(r"\.", '', text)
    text = re.sub(r'网页链接', '', text)
    text = re.sub(r'&nbsp;', '', text)
    text = text.strip()
    return text


def process_time(create_time):
    create_time = create_time.split(' ')
    year = create_time[5]
    month = str(list(calendar.month_abbr).index(create_time[1]))
    if len(month) == 1:
        month = '0' + month
    day = create_time[2]
    time_ = year + month + day
    return int(time_)


def is_target(text):
    flag = False
    # TODO 这里在切换要爬取的公司时所筛选的tag，需要替换
    if '<a href="http://xueqiu.com/S/SH601633" target="_blank">$长城汽车(SH601633)$</a>' or \
            '<a href="http://xueqiu.com/S/02333" target="_blank">$长城汽车(02333)$</a>' in text:
        flag = True
    return flag


def get_data(stock_name, symbol, contents):
    for content in contents:
        text = content['text']
        if not is_target(text):
            print("该评论没有提及该公司，抛弃")
            continue
        text = re.sub(r'<.+?>', '', text.strip())
        if len(text) > 400:
            print("该评论太长，抛弃")
            continue
        try:
            text = process_text(text)
            print(text)
            like_count = int(content['like_count'])
            track_json = json.loads(content['trackJson'])
            reply_count = int(track_json['reply_count'])
            retweet_count = int(track_json['retweet_count'])
            create_time = track_json['created_at']
            create_time = process_time(create_time)
            all_list = [stock_name, symbol, text, like_count, retweet_count, reply_count, create_time]
            store_csv(all_list)
        except :
            print('解码错误')
            continue


def get_comments(symbol, Page):
    url = "https://xueqiu.com/query/v1/symbol/search/status"
    header = {
        "cookie": "acw_tc=2760820516209148172186278e837f50cc1423f5df30d4207d0fd611c5d0a9; xq_a_token=4b4d3f5b97e67b975f4e1518dc4c417ebf0ad4c4; xqat=4b4d3f5b97e67b975f4e1518dc4c417ebf0ad4c4; xq_r_token=960e1d453ab676f85fa80d2d41b80edebfde8cc0; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTYyMjUxNTc5MiwiY3RtIjoxNjIwOTE0NzgyMjMyLCJjaWQiOiJkOWQwbjRBWnVwIn0.WS7CtcU_zK9hnwLwDM6RgSDotUkmoePt9Zp1tVMtle2t8ZaYOWIIts8BjOmCzBXcDuhU6N03caYAc_91pMm-sPDPyFNRc_CEcXu6GFHk0zDoSlGDUeUF-DmY6KBzAdFW9rUyfCu7D67sA4WQIyHF3l5TrhHr7R2KeTPZ9ZkYuQpKus03ozZIDvObBO5Stl-Vw3ojuK6H6dag-zotH5Kimgsi5SdY3OqdTY3mlLsRuPFqTkSO55h5SPdeOV5vNavJL1sQc6ZmqYq_SRxYFggUmQlasYUcKnOoQ9SngBQKE8n3dDL-ctggxOAxYw4hTrv_ochZ_DMK1mALPC_83XGL9w; u=571620914817231; Hm_lvt_1db88642e346389874251b5a1eded6e3=1620914820; device_id=24700f9f1986800ab4fcc880530dd0ed; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1620915727",
        "referer": "https://xueqiu.com",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.56 Safari/537.36",
    }
    params = {
        "u": user,
        "symbol": symbol,
        "access_token": access_token,
        "page": Page
    }
    response = requests.get(url, params, headers=header).content.decode('utf-8')
    response = json.loads(response)
    contents = response['list']
    get_data(query, symbol, contents)


def init():
    stock_index = []
    url = "https://xueqiu.com/query/v1/search/web/stock.json"
    header = {
        "cookie": "acw_tc=2760820516209148172186278e837f50cc1423f5df30d4207d0fd611c5d0a9; xq_a_token=4b4d3f5b97e67b975f4e1518dc4c417ebf0ad4c4; xqat=4b4d3f5b97e67b975f4e1518dc4c417ebf0ad4c4; xq_r_token=960e1d453ab676f85fa80d2d41b80edebfde8cc0; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTYyMjUxNTc5MiwiY3RtIjoxNjIwOTE0NzgyMjMyLCJjaWQiOiJkOWQwbjRBWnVwIn0.WS7CtcU_zK9hnwLwDM6RgSDotUkmoePt9Zp1tVMtle2t8ZaYOWIIts8BjOmCzBXcDuhU6N03caYAc_91pMm-sPDPyFNRc_CEcXu6GFHk0zDoSlGDUeUF-DmY6KBzAdFW9rUyfCu7D67sA4WQIyHF3l5TrhHr7R2KeTPZ9ZkYuQpKus03ozZIDvObBO5Stl-Vw3ojuK6H6dag-zotH5Kimgsi5SdY3OqdTY3mlLsRuPFqTkSO55h5SPdeOV5vNavJL1sQc6ZmqYq_SRxYFggUmQlasYUcKnOoQ9SngBQKE8n3dDL-ctggxOAxYw4hTrv_ochZ_DMK1mALPC_83XGL9w; u=571620914817231; Hm_lvt_1db88642e346389874251b5a1eded6e3=1620914820; device_id=24700f9f1986800ab4fcc880530dd0ed; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1620915727",
        "referer": "https://xueqiu.com",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.56 Safari/537.36",
    }
    params = {
        "q": query,
    }
    response = requests.get(url, params, headers=header).content.decode('utf-8')
    response = json.loads(response)
    contents = response['list']
    for content in contents:
        stock_index.append(content['code'])
    return stock_index


if __name__ == '__main__':
    fp = open(r'C:\Users\10155\Desktop\stock_comment.csv', 'a', encoding='gbk', newline='')
    csv_writer = csv.writer(fp)
    csv_writer.writerow(['公司名称', '股票代码', '评论内容', '评论点赞', '评论转发', '评论回复', '评论时间'])
    fp.close()
    # 初始化需要抓取的股票代码
    stock_index_list = init()
    # TODO 如果出现访问失败，则查看最新爬取的页面，更改range中的参数
    for index in stock_index_list:
        for page in range(1, 100):
            print(page)
            time.sleep(1)
            get_comments(index, str(page))
