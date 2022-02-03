import React from "react";
import axios from 'axios';
import UploadFile from "./UploadFile";
import ReactDOM from 'react-dom';
class ListFiles extends React.Component{
      state = {
         files: []
      };
      componentDidMount() {

             // Request made to the backend api
      // Send formData object
      axios.get("http://localhost:8080/listFiles", { // receive two parameter endpoint url ,form data 
   })
   .then(res => { // then print response status
     
      // console.warn(res);
       
        this.setState({files: res.data});
       
   });
      }
    renderTableData() {
        return this.state.files.map((theFile, index) => {
           const { fileDownloadUri, fileName, fileStatus, fileType, size } = theFile //destructuring
           return (
              <tr key={index}>
                 <td>{fileName}</td>
                 <td><a href={fileDownloadUri}>{fileDownloadUri}</a></td>
              </tr>
           )
        });
    
     }

     goBack(){
        ReactDOM.render(<UploadFile />, document.getElementById('theContainer'));
    }
  
     render() {
        return (
           <div className="container">
            
            <div  style={{marginTop: 20}}>
            
            <button type="submit" style={{marginLeft: 20}} onClick={()=>this.goBack()} >Back</button>
            </div>
              <h3 id='title'>All files uploaded</h3>
              <table className="table">
              <thead className="thead-light">
               <tr>
                  <th scope="col">File Name</th>
                  <th scope="col">Download URL</th>
         
               </tr>
            </thead>
                 <tbody>
                    {this.renderTableData()}
                 </tbody>
              </table>
           </div>
        )
     }
}

export default ListFiles;