export class News {
  publishedOn: Date;
  headline: string;
  content: string;
  author: string;
  editable: boolean;
  editDialog: boolean;
  id: number;

  static fromObject(object: any): News {
    const n = new News();
    n.headline = object.headline;
	n.content = object.content;
	n.author = object.author;
	n.publishedOn = new Date(parseInt(object.publishedOn));
	n.id = object.id;
	n.editable = false;
	n.editDialog = false;
    return n;
  }
}
