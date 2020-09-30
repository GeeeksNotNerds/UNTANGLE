const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { response } = require('express');

admin.initializeApp(functions.config().firebase);

exports.sendNotificationToMultiUsers = functions.database.ref("MultiNotifications/{uid}/{notification_id}")
.onWrite((data, context) => {

  
  const notification_id = context.params.notification_id;

  

  if(!data.after.val()){
      console.log(' notification has been deleted : ',notification_id);
      return null;
  }


  var topic = 'students';

  const payload = {
    notification:{
        title : "Announcement",
        body : `New Post by Admin`,
        icon : "default"
    }
};
  
  

      return admin.messaging().sendToTopic(topic,payload)
      .then(response =>{

          console.log('Notification sent!');

      });
  });





