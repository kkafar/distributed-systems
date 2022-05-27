use hello::say_client::SayClient;
use hello::SayRequest;

pub mod hello {
  tonic::include_proto!("hello");
}


#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
  let channel = tonic::transport::Channel::from_static("http://[::1]:50051")
      .connect()
      .await?;

  let mut client = SayClient::new(channel);
  let request = tonic::Request::new(
    SayRequest {
      name: String::from("stub message"),
      id: 10,
    },
  );

  let response = client.send(request).await?.into_inner();
  println!("Response = {:?}", response);

  Ok(())
}