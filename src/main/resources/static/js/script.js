console.log("This is js File")
const toggleSidebar=()=>{
	if($('.sidebar').is(":visible"))
	{
		//true
		//$(".sidebar").css("display","none");
		$(".sidebar").hide("slow");
		$(".content").css("margin-left","0%");
		//$(".sidebar").css("transition","all 0.5s");
		
		 
	}else{
		//false
		//$(".sidebar").css("display","block");
		
		$(".content").css("margin-left","20%");
		$(".sidebar").show("slow");
		//$(".sidebar").fadein("slow");
	}
};
const search=()=>{
	//alert("Searching...");
	let query=$("#search-input").val();

	if(query=="")
	{	
		$(".search-result").hide();
	}else{
		//sending Request to server
		
		let url='http://localhost:8080/search/'+query;
		//fetch api 
		fetch(url)
		  .then((response)=>{
			//get data from api in json and return to another then which is written below at line 34
			return response.json();
		}).then((data)=>{
			//data...
			console.log(data);
			let text=`<div class='list-group'>`;
			   data.forEach((contact)=>{
				text+=`<a href='/user/${contact.cid}/contact' class="list-group-item list-group-action">
				${contact.name}
				</a>`
			});
			text+=`</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
		});
	}
};