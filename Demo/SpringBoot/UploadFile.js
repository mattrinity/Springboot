import axios from 'axios';
import React,{Component} from 'react';
import ListFiles from './ListFiles';
import ReactDOM from 'react-dom';
class UploadFile extends Component {
  
    state = {
 
      // Initially, no file is selected
      selectedFile: null,
      fileURL : "",
      isVisible: false
    };
    
    // On file select (from the pop up)
    onFileChange = event => {
    
      // Update the state
      this.setState({ selectedFile: event.target.files[0] });
    
    };
    
    renderListFiles(){
        debugger
        //<ListFiles />
        ReactDOM.render(<ListFiles />, document.getElementById('theContainer'));
    }
    // On file upload (click the upload button)
    onFileUpload = () => {
    
      // Create an object of formData
      const formData = new FormData();
    
      // Update the formData object
      formData.append(
        "file",
        this.state.selectedFile,
        this.state.selectedFile.name
      );
    
      // Details of the uploaded file
      console.log(this.state.selectedFile);
    
      // Request made to the backend api
      // Send formData object
      axios.post("http://localhost:8080/uploadFile", formData, { // receive two parameter endpoint url ,form data 
    })
    .then(res => { // then print response status
      
        console.warn(res.fileDownloadUri);
        debugger
        this.setState({fileURL: res.data.fileDownloadUri});
        this.setState({isVisible:true});
    });
    };
    
    // File content to be displayed after
    // file upload is complete
    fileData = () => {
    
      if (this.state.selectedFile) {
         
        return (
          <div>
            <h2>File Details:</h2>
             
<p>File Name: {this.state.selectedFile.name}</p>
 
             
<p>File Type: {this.state.selectedFile.type}</p>
 
             
<p>
              Last Modified:{" "}
              {this.state.selectedFile.lastModifiedDate.toDateString()}
            </p>
           
            {this.state.isVisible ? <div className='container text-center jumotron' style={{marginTop: 20, }}>
                    <h3>Click to download the translated file</h3>
                    <a href = {this.state.fileURL}>{this.state.fileURL}</a></div> : null}  
          </div>
        );
      } else {
        return (
          <div>
            <br />
            <h4>Choose before Pressing the Upload button</h4>
          </div>
        );
      }
    };
    
    render() {
    
      return (
        <div>
            <h1>
              Welcome
            </h1>
            <h3>
              Please upload word file!
            </h3>
            <div>
                <input type="file" onChange={this.onFileChange} />
                <button onClick={this.onFileUpload}>
                  Upload!
                </button>
            </div>
          {this.fileData()}

          <button type="submit" style={{marginTop: 20}} onClick={()=>this.renderListFiles()} >List All Files</button>
      
        </div>
      );
    }
  }

  export default UploadFile;