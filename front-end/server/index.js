const express = require('express')
const path = require('path')
const app = express()
app.use(express.static(path.join(__dirname, 'dist')))
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/index.html'))
})

app.listen(3000, function() {
  console.log('Vue app listeneing')
})
