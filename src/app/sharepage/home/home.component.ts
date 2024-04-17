import { Component, OnInit } from '@angular/core';
import { CourseDetailsService } from 'src/app/services/course-details.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit{

  constructor(private service:CourseDetailsService) {}
  courseData:any;
  ngOnInit(): void {
    this.courseData = this.service.courseDetails;
  }
  
}
