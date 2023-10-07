export function formatTime(time) {//传入时间
  const d = new Date(time)
  // console.log(d)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) { // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  } else if (diff<3600*24*14){
    return '1周前'
  }
  else if (diff<3600*24*60){
    return '1月前'
  }
  else if(diff<3600*24*365*2){
    return '1年前'
  }
  return time
}


export function formatDate(value) {//传入时间戳
  let date = new Date(value);
  let y = date.getFullYear();
  let MM = date.getMonth() + 1;
  MM = MM < 10 ? '0' + MM : MM;
  let d = date.getDate();
  d = d < 10 ? '0' + d : d;
  let h = date.getHours();
  h = h < 10 ? '0' + h : h;
  let m = date.getMinutes();
  m = m < 10 ? '0' + m : m;
  let s = date.getSeconds();
  s = s < 10 ? '0' + s : s;
  return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
}
