import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { environment } from '../../environments/environment';
@Injectable()
export class UploadDialogService {

    private url:string=environment.API.URL+':'+environment.API.PORT;
    constructor(private http: Http) {
       
    }
    registerAsset(file): Observable<any[]> {
        // let headers = new Headers({ 'Content-Type': 'application/json' });
        // let options = new RequestOptions({ headers: headers });

        return this.http.post(this.url+'/assets/registerasset ', 
       file
    )
    .map((response: Response) => {       
        return <any[]>response.json();
     })
     .do(data => console.log(data)) // to peak into response data
     .catch(this.handleError);

    }

    uploadFile(id,file): Observable<any[]> {
        // let headers = new Headers({ 'Content-Type': 'application/json' });
        // let options = new RequestOptions({ headers: headers });

        return this.http.post(this.url+'/asset/uploadfile/'+id,
        file
       
    )
    .map((response: Response) => {       
        return <any[]>response.json();
     })
     .do(data => console.log(data)) // to peak into response data
     .catch(this.handleError);

    }

    private handleError(error: Response){
        console.log(error);
        return Observable.throw(error.json().error||"Server error");
        // this.notify.openSnackBar("error!", "");
    }


}
