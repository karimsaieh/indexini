<template>
  <div style="border: 1px solid">

    file similar
    <el-row>
      <el-col justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src=""

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
        <a href="">Text file title or link you know </a>
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="https://is4-ssl.mzstatic.com/image/thumb/Purple118/v4/be/37/93/be379385-c465-70b6-ebfb-44d7eb48f0f9/AppIcon-1-1x_U007emarketing-85-220-6.png/246x0w.jpg"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/PDF_file_icon.svg/833px-PDF_file_icon.svg.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="data:image/jpeg;base64,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"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="http://wvs.topleftpixel.com/photos/scotia_plaza_tall_stitched.jpg"

          icon-src="http://icons.iconarchive.com/icons/dakirby309/simply-styled/256/Microsoft-Word-2013-icon.png"
        />
      </el-col>
      <el-col class="center" justify="center" align="middle" :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
        <file-thumbnail
          image-src="https://soundsofsash.files.wordpress.com/2009/02/tall-building-1.jpg"

          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        /></el-col>
    </el-row>
    <el-row>
      <el-col justify="center" align="middle">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="1000"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import FileThumbnail from './FileThumbnail'

export default {
  name: 'FileSimilarFiles',
  components: {
    FileThumbnail

  }
}
</script>

<style scoped>
a{
  text-decoration: underline
}
/* unvisited link */
a:link {
  color: red;
}

/* visited link */
a:visited {
  color: green;
}

/* mouse over link */
a:hover {
  color: hotpink;
}

/* selected link */
a:active {
  color: blue;
}
</style>

