<template>
    <div class="content">
        <vue-waterfall-easy srcKey="imgUrl" :imgsArr="imgsArr" @scrollReachBottom="getData" class="waterfull"
            ref='waterfall' :maxCols=4 :loadingTimeOut=2000 :height="680">
            <div slot="waterfall-head" style="text-align: center;"></div>
            <div class="img-info" slot-scope="props" style="box-shadow: 0 2px 4px rgba(0,0,0,0.2); border-radius: 10px">
                <div class="user" style="padding: 12px; margin-top: 5px;">
                    <div class="info">{{ props.value.picalbumContent }}</div>
                    <div class="infoUser" style="margin-top: 20px;">
                        <a :href="'/personal/footprint/' + props.value.userNid"
                            style="display: flex; align-items: center; color: inherit;">
                            <img class="infoUserimg"
                                style="border-radius: 50%; height: 20px; width:20px; line-height: 2; margin-right:5px"
                                :src=props.value.userAvatarUrl />
                            <span>
                                <a :href="'/personal/footprint/' + props.value.userNid"> {{ props.value.userName }}</a>
                            </span>
                        </a>

                        <i class="fa-solid fa-heart" :class="{ 'liked': isLiked[props.value.picalbumNid] == 1 }"
                            @click="changePicAlbumLike(props.value.picalbumNid)"> {{ props.value.likeCount }}</i>
                    </div>

                </div>
            </div>
            <div slot="waterfall-over">已经到底啦</div>
        </vue-waterfall-easy>
    </div>
</template>
<script>
import vueWaterfallEasy from 'vue-waterfall-easy'
import axios from 'axios'
import { AllpicAlbums } from '../../api/picAlbum'
import { checkPicAlbumLike, delPicAlbumLike, addPicAlbumLike } from '../../api/like'
export default {
    name: 'Flow',
    data() {
        return {
            imgsArr: [],
            last: -1,
            queryInfo: {
                pagenum: 1,
                pagesize: 10
            },
            tableData: [],
            isLiked: {},
            idx: {}
        }
    },
    components: {
        vueWaterfallEasy
    },
    methods: {
        async getData() {
            try {
                const res = await AllpicAlbums(this.queryInfo)
                if (res.success == false) {
                    this.$refs.waterfall.waterfallOver()
                    return
                }
                this.tableData = res.data.list
                var tmp = -1;
                res.data.list.forEach(element => {
                    tmp = element.picalbumNid
                })
                if (tmp == this.last) {
                    this.$refs.waterfall.waterfallOver()
                    return
                }
                this.last = tmp
                this.imgsArr = this.imgsArr.concat(this.tableData)
                for (var i = 0; i < this.imgsArr.length; i++) {
                    this.idx[this.imgsArr[i].picalbumNid] = i;
                }
                this.queryInfo.pagenum++;
                //5-22:
                for (var i = 0; i < this.tableData.length; i++) {
                    this.isLiked[this.tableData[i].picalbumNid] = 0;
                }
                if (this.$store.state.id) {
                    for (var i = 0; i < this.tableData.length; i++) {
                        checkPicAlbumLike({
                            userId: this.$store.state.id,
                            picAlbumId: this.tableData[i].picalbumNid,
                        })
                            .then((res) => {
                                this.isLiked[res.data.id] = res.data.res;
                                this.$forceUpdate();
                            })
                    }
                }

            } catch (err) {
                console.log(err)
            }
        },
        //5-22
        changePicAlbumLike(picAlbumId) {
            if (!this.$store.state.id) {
                return;
            }
            if (this.isLiked[picAlbumId] == 0) {
                addPicAlbumLike({
                    userId: this.$store.state.id,
                    picAlbumId: picAlbumId,
                })
                this.imgsArr[this.idx[picAlbumId]].likeCount++;
            } else {
                delPicAlbumLike({
                    userId: this.$store.state.id,
                    picAlbumId: picAlbumId,
                })
                this.imgsArr[this.idx[picAlbumId]].likeCount--;
            }
            this.isLiked[picAlbumId] ^= 1;

        }

    },
    created() {
        this.getData()
    }
}
export class Img {
    constructor({
        name,
        desc,
    }) {
        this.desc = desc
        this.name = name
    }
}
export function CreateImg(imgData) {
    return new Img({
        desc: imgData.desc,
        name: imgData.name
    })
}
</script>
<style> #content {
     position: absolute;
     /*必须*/
     top: 800px;
     /*top必须，大小可控制*/
     bottom: 100px;
     /*bottom必须，一直延申到当前块底部*/
     width: 100%;
 }

 .vue-waterfall-easy-container {
     position: fixed !important;
     width: 1320px;
     height: 800px;
     overflow: hidden;
     margin: 0 auto;
 }

 .vue-waterfall-easy-scroll {
     width: 1340px;
     height: 100%;
     overflow-x: hidden;
     overflow-y: scroll;
 }

 .infoUser {
     display: flex;
     align-items: center;
     justify-content: space-between;
     height: 20px;
     color: #666;
     font-size: 12px;
     transition: color 1s;
 }

 /* 5-22 */
 .liked {
     color: red;
 }

 .vue-waterfall-easy-container .vue-waterfall-easy a.img-inner-box[data-v-ded6b974] {
     border-radius: 10px !important;
 }

 .vue-waterfall-easy-container .vue-waterfall-easy a.img-wraper>img {
     border-radius: 10px 10px 0 0 !important;
 }
</style>
<style lang="scss">
::-webkit-scrollbar {
    width: 0 !important;
}

::-webkit-scrollbar {
    width: 0 !important;
    height: 0;
}

.content {
    padding: 20px;
}
</style>