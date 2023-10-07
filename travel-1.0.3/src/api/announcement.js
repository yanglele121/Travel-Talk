import request from '@/request'

export function Announcement(queryInfo){
    return request({
        url:'/Announcement/SysAnnouncement',
        method:'post',
        data: queryInfo
    })
}
export function NewAnnouncement(announcement){
    return request({
        url:'/Announcement/NewAnnouncement',
        method: 'post',
        data: announcement
    })
}

export function UpdateAnnouncement(announcement){
    return request({
        url:'/Announcement/UpdateNewAnnouncement',
        method: 'post',
        data: announcement
    })
}
export function TopFiveAnnouncement(){
    return request({
        url:'/Announcement/TopAnnouncement',
        method: 'get'
    })
}

