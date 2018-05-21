import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { environment } from '../../environments/environment';
@Injectable()
export class RegisterService {

    private url:string=environment.API.URL+':'+environment.API.PORT;
    constructor(private http: Http) {
       
    }

    registerUser(userData): Observable<any[]> {
        console.log(userData);
        // let headers = new Headers({ 'Content-Type': 'application/json' });
        // let options = new RequestOptions({ headers: headers });

        return this.http.post(this.url+'/actor/actorregistration', 
            userData
    )
    .map((response: Response) => {  
        console.log(response);     
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
