using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace SitMeThrough
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private MqttClient client;
        int pressure1   = 0;
        int pressure2   = 0;
        int pressure3   = 0;
        int pressure4   = 0;
        int temperature = 0;
        int distance    = 0;
        int angle = 0;

        public object Thread { get; private set; }

        public MainPage()
        {
            client = new MqttClient("ec2-54-218-1-206.us-west-2.compute.amazonaws.com", 2880, false, MqttSslProtocols.None);
            client.MqttMsgPublishReceived += Client_MqttMsgPublishReceived;
            string clientId = Guid.NewGuid().ToString();
            client.Connect(clientId);
            client.Subscribe(new string[] { "/smartchair/data/spark", "/smartchair/data/gallileo", "/smartchair/data/angle" }, new byte[] { uPLibrary.Networking.M2Mqtt.Messages.MqttMsgBase.QOS_LEVEL_EXACTLY_ONCE, uPLibrary.Networking.M2Mqtt.Messages.MqttMsgBase.QOS_LEVEL_EXACTLY_ONCE, uPLibrary.Networking.M2Mqtt.Messages.MqttMsgBase.QOS_LEVEL_EXACTLY_ONCE });
            var t = Task.Run(() => sendconsolidateddata());
            //t.Wait();
            this.InitializeComponent();
        }
        private async Task sendconsolidateddata()
        {
            while (true)
            {
                consolidateddata cd = new consolidateddata();
                cd.angle = angle;
                cd.distance1 = distance;
                cd.distance2 = distance;
                cd.pressure1 = pressure1;
                cd.pressure2 = pressure2;
                cd.pressure3 = pressure3;
                cd.pressure4 = pressure4;
                string sToSend = JsonConvert.SerializeObject(cd);
                client.Publish("/smartchair/data", Encoding.UTF8.GetBytes(sToSend));
                Task.Delay(2000).Wait();
            }
            
        }
        private void Client_MqttMsgPublishReceived(object sender, MqttMsgPublishEventArgs e)
        {
            try
            {
                String mqttMessage = System.Text.Encoding.UTF8.GetString(e.Message);
                if (e.Topic == "/smartchair/data/spark")
                {
                    Debug.WriteLine("spark");
                    sparkdata sd = JsonConvert.DeserializeObject<sparkdata>(mqttMessage);
                    this.pressure1 = sd.pressure1;
                    this.pressure2 = sd.pressure2;
                    this.pressure3 = sd.pressure3;
                    this.pressure4 = sd.pressure4;
                    this.distance = sd.Distance;
                }
                else if (e.Topic == "/smartchair/data/gallileo")
                {
                    Debug.WriteLine("gallileo");
                    distancedata dd = JsonConvert.DeserializeObject<distancedata>(mqttMessage);
                    this.distance = dd.distance1;    
                }
                else if (e.Topic == "/smartchair/data/angle")
                {
                    Debug.WriteLine("angle");
                    angledata ad = JsonConvert.DeserializeObject<angledata>(mqttMessage);
                    this.angle = ad.angle;
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.ToString());
              //  throw;
            }
            
        }
    }
    class sparkdata
    {
        public int pressure1;
        public int pressure2;
        public int pressure3;
        public int pressure4;
        public int temperature;
        public int Distance;       
    }
    class consolidateddata
    {
        public int pressure1;
        public int pressure2;
        public int pressure3;
        public int pressure4;
        public int temperature;
        public int distance1;
        public int distance2;
        public int angle;
    }
    class angledata
    {
        public int angle;
    }
    class distancedata
    {
        public int distance1;
        public int distance2;
    }
}
