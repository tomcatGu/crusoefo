import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url('/hello2') {
            queryParameters {
                parameter("name", "zhangsan")
            }
        }

    }
    response {
        status 200
        body("""
  {
    "code": "000000",
    "mesg": "abc"
  }
  """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}