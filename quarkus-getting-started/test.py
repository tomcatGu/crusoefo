import asyncio
from requests_html import HTMLSession
from requests_html import AsyncHTMLSession

import time
#启动
session = HTMLSession()
#获取
fail=0
for i in range(1,2):
    tongzhou= session.get('http://www.tongzhou.gov.cn/')
    print(tongzhou.status_code)
    if(tongzhou.status_code!=200):
        fail=fail+1
    #time.sleep(1)
print(fail)
#print(tongzhou.status_code)
#tongzhou.html.render()
# 获取网页内的所有链接
link_list = tongzhou.html.links
# 获取页面上所欲偶的链接，以绝对路径的方式
ab_link_list = tongzhou.html.absolute_links
#print(ab_link_list)

log= session.get('http://log.nantong.gov.cn/sso/login')
try:
    log.html.render(keep_page = True)
    async def main():

        await log.html.page.waitFor(1000)
        await log.html.page.type(' [id="username"]','13962847336')
        await log.html.page.type(' [id="password"]','13962847336')
        await log.html.page.click(' [id = "checkAndGoSystem"]')
        #log.html.render(keep_page = True)
        await log.html.page.waitFor(1000)

        await log.html.page.reload()
        #await log.html.page.screenshot({'path': 'login.png'})
        await log.html.page.click(' [id = "sys_menu"]')

        #await log.html.page.click(' [class = "m125"]')
        await log.html.page.waitFor(10000)
        #content=await log.html.page.content()
        await log.html.page.screenshot({'path': 'login.png'})
    log=session.loop.run_until_complete(main())


except Exception as e:
    print(e)
finally:
    session.close()
