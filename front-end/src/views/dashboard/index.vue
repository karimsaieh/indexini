<template>
  <div>

    <div class="fixed-width-page-parent-container">
      <div class="fixed-width-page-container">

        <el-row :gutter="40">
          <el-col :span="6">
            <el-card class="a-card">
              <div class="card-icon">
                <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
              </div>
              <div class="card-description">
                <div class="card-panel-text">Fichiers Indexés</div>
                <div class="card-panel-num">
                  <count-to :start-val="0" :end-val="102400" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card class="a-card">
              <div class="card-icon">
                <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
              </div>
              <div class="card-description">
                <div class="card-panel-text">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card class="a-card">
              <div class="card-icon">
                <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
              </div>
              <div class="card-description">
                <div class="card-panel-text">Fichiers Indexés</div>
                <div class="card-panel-num">5648,48 K</div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card class="a-card">
              <div class="card-icon">
                <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
              </div>
              <div class="card-description">
                <div class="card-panel-text">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <br>
        <el-row :gutter="40">
          <el-col :span="6">
            <el-card style="font-weight: bold">
              <div style="text-align:center">
                <div style="font-size: 2.8rem;">
                  <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-text" style="margin-top: 10px">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>

              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card style="font-weight: bold">
              <div style="text-align:center">
                <div style="    font-size: 2.8rem;">
                  <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-text" style="margin-top: 10px">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>

              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card style="font-weight: bold">
              <div style="text-align:center">
                <div style="    font-size: 2.8rem;">
                  <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-text" style="margin-top: 10px">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>

              </div>
            </el-card>
          </el-col>

          <el-col :span="6">
            <el-card style="font-weight: bold">
              <div style="text-align:center">
                <div style="    font-size: 2.8rem;">
                  <i class="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-text" style="margin-top: 10px">Fichiers Indexés</div>
                <div class="card-panel-num">5648,584,8448 K</div>

              </div>
            </el-card>
          </el-col>

        </el-row>

        <br>

      </div>
    </div>

    <router-link :to="{ name: 'Dashboard', query: { plan: 'private' }, params: { disableTransition: true } }">
      Go to Foo
    </router-link>
    <br>
    <router-link to="/?q=6">Go to Bar</router-link>
    dasgboard container
    <file-search-card />

  </div>

</template>

<script>
import CountTo from 'vue-count-to'
import FileSearchCard from '@/components/File/FileSearchCard'
// import { indexingStats } from '@/api/file'
// import { sparkStats } from '@/api/spark-manager'

export default {
  name: 'Dashboard',
  components: {
    FileSearchCard,
    CountTo
  }
  // created() {
  //   // indexingStats().then((result) => {
  //   //   this.indexingStats = Object.assign({}, this.indexingStats, result.data)
  //   // })
  //   // sparkStats().then((result) => {
  //   //   this.sparkstats = Object.assign({}, this.sparkstats, result.data)
  //   //   this.sparkstats.lastJobDate = new Date(this.sparkstats.lastJobDate)
  //   // })
  // }
}

</script>

<style lang="scss" scoped>
.number {
  color: #666;
  font-weight: 900;
  font-size: 30px
}

.a-card {
  height: 100px;
}
  .icon-file {
    color: rebeccapurple
  }
  .card-icon {
    font-size: 2.8rem;
    float: left;
    margin: 5px 0px 10px 0px;
  }
  .card-description {
    float: right;
    font-weight: bold;

  }
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        color: #666;
        font-size: 20px;
      }
</style>

