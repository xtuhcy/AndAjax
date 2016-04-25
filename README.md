# AndAjax
在Andorid中模拟Ajax方式异步调用http请求，修改界面。

##实例化
必须要Activity、Fragment等UI线程上实例化AndAjax，下面是实例化一个有2个线程的AndAjax。

	AndAjax $ = AndAjax.thread(2);

##json、xml调用方式
	
    $.ajax("url", new AndAjax.Callback<String>() {
        @Override
        public void onSuccess(String json) {

        }

        @Override
        public void onError(int state, Exception ex) {

        }
    });
##图片调用方式
	$.image("url", new AndAjax.Callback<Bitmap>() {
        @Override
        public void onSuccess(Bitmap o) {
            
        }

        @Override
        public void onError(int state, Exception ex) {
            
        }
    });
